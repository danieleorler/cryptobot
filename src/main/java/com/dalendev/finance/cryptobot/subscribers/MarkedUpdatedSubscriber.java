package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.*;
import com.dalendev.finance.cryptobot.model.events.MarketUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrdersSelectedEvent;
import com.dalendev.finance.cryptobot.model.events.PositionsCreatedEvent;
import com.dalendev.finance.cryptobot.singletons.Counters;
import com.dalendev.finance.cryptobot.singletons.Exchange;
import com.dalendev.finance.cryptobot.singletons.Market;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import com.dalendev.finance.cryptobot.util.PriceUtil;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author daniele.orler
 */

@Component
public class MarkedUpdatedSubscriber {

    private Log logger = LogFactory.getLog(MarkedUpdatedSubscriber.class);

    private final Market market;
    private final Portfolio portfolio;
    private final EventBus eventBus;
    private final Counters counters;
    private final OrderAdapter orderAdapter;
    private final Exchange exchange;

    @Autowired
    public MarkedUpdatedSubscriber(Market market, EventBus eventBus, Portfolio portfolio, Counters counters, OrderAdapter orderAdapter, Exchange exchange) {
        this.market = market;
        this.portfolio = portfolio;
        this.eventBus = eventBus;
        this.counters = counters;
        this.orderAdapter = orderAdapter;
        this.exchange = exchange;
        eventBus.register(this);
    }

    @Subscribe
    public void selectOrders(MarketUpdatedEvent event) {
        List<Order> orders = this.market.getMarket().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(c -> c.getChange() > 5)
            .sorted((c1, c2) -> Float.compare(c2.getChange(), c1.getChange()))
            .map(c -> {
                Float stepSize = exchange.getLotFilter(c.getSymbol()).getStepSize();
                Float quantity = PriceUtil.adjust(0.001f / c.getLatestPrice(), stepSize);
                return new Order.Builder()
                    .symbol(c.getSymbol())
                    .side(Order.Side.BUY)
                    .type(Order.Type.MARKET)
                    .quantity(quantity)
                    .price(c.getLatestPrice())
                    .status(Order.Status.TO_BE_PLACED)
                    .build();
            })
            .collect(Collectors.toList());

        if(orders.size() > 0) {
            OrdersSelectedEvent ordersSelectedEvent = new OrdersSelectedEvent();
            ordersSelectedEvent.getOrders().addAll(orders);
            eventBus.post(ordersSelectedEvent);
        }
    }

    @Subscribe
    public void placeOrders(OrdersSelectedEvent event) {
        event.getOrders().stream()
            .filter(order -> !portfolio.getPositions().containsKey(order.getSymbol()))
            .forEach(order -> {
                try {
                    Long orderId = orderAdapter.placeOrder(order);
                    order.setId(orderId);
                    order.setStatus(Order.Status.PLACED);
                    logger.debug(order);
                } catch (Exception e) {
                    logger.error("Error placing order: " + order);
                    logger.error(e);
                }
            });
    }

    @Subscribe
    public void updatePositions(PositionsCreatedEvent event) {
        this.portfolio.getPositions().entrySet().stream()
            .map(Map.Entry::getValue)
            .forEach(position -> {
                CryptoCurrency crypto = market.getMarket().get(position.getSymbol());
                Float currentPrice = crypto.getLatestPrice();
                Float openPrice = position.getOpenPrice();
                position.setChange(PriceUtil.change(openPrice, currentPrice));
                System.out.println(String.format("Position on %s: start %.8f now %.8f -> %.4f%%",
                        position.getSymbol(),
                        position.getOpenPrice(),
                        crypto.getLatestPrice(),
                        position.getChange()));
            });

        this.portfolio.getPositions().entrySet()
            .removeIf(entry ->  {
                Position position = entry.getValue();
                if(shouldClosePosition(position, 5f)) {
                    logger.debug(String.format("Closed position on %s with result %.4f", position.getSymbol(), position.getChange()));
                    Float finalPrice = PriceUtil.addPercentage(position.getOpenPrice(), position.getChange());
                    counters.addToProfit(finalPrice - position.getOpenPrice());
                    System.out.println("Profit balance: " + counters.getProfit() + " BTC");
                    return true;
                }
                return false;
            });
    }


    private Boolean shouldClosePosition(Position position, Float gain) {

        if(position.getChange() > gain) {
            return true;
        }
        return false;
    }

}
