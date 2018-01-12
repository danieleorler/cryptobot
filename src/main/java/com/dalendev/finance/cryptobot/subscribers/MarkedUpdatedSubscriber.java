package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.*;
import com.dalendev.finance.cryptobot.model.events.*;
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
    public void selectBuyOrders(PositionsUpdatedEvent event) {
        List<Order> orders = this.market.getMarket().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(crypto -> !portfolio.getPositions().containsKey(crypto.getSymbol()))
            .filter(crypto -> !portfolio.getOrders().containsKey(crypto.getSymbol()))
            .filter(crypto -> crypto.getChange() > 1)
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
    public void selectSellOrders(PositionsUpdatedEvent event) {
        List<Order> orders = portfolio.getPositions().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(position -> position.getChange() > 1)
            .map(p -> new Order.Builder()
                .symbol(p.getSymbol())
                .side(Order.Side.SELL)
                .type(Order.Type.MARKET)
                .quantity(p.getAmount())
                .price(PriceUtil.addPercentage(p.getOpenPrice(), p.getChange()))
                .status(Order.Status.TO_BE_PLACED)
                .build()
            ).collect(Collectors.toList());

        if(orders.size() > 0) {
            OrdersSelectedEvent ordersSelectedEvent = new OrdersSelectedEvent();
            ordersSelectedEvent.getOrders().addAll(orders);
            eventBus.post(ordersSelectedEvent);
        }
    }

    @Subscribe
    public void placeOrders(OrdersSelectedEvent event) {
        event.getOrders()
            .forEach(order -> {
                try {
                    logger.debug(order);
                    Long orderId = orderAdapter.placeOrder(order);
                    order.setId(orderId);
                    order.setStatus(Order.Status.PLACED);
                    eventBus.post(new OrderUpdatedEvent(order));
                } catch (Exception e) {
                    logger.error("Error placing order: " + order);
                    logger.error(e);
                }
            });
    }

    @Subscribe
    public void moveOrder(OrderUpdatedEvent event) {
        Order order = event.getOrder();
        switch (order.getStatus()) {
            case PLACED:
                portfolio.getOrders().put(order.getSymbol(), order);
                logger.debug(order);
                break;
            case FILLED:
                switch (order.getSide()) {
                    case BUY:
                        portfolio.getOrders().remove(order.getSymbol());
                        portfolio.getPositions().put(order.getSymbol(), new Position(order));
                        logger.debug(order);
                        break;
                    case SELL:
                        portfolio.getOrders().remove(order.getSymbol());
                        Position position = portfolio.getPositions().remove(order.getSymbol());
                        counters.addToProfit(PriceUtil.addPercentage(position.getOpenPrice(), position.getChange()) - position.getOpenPrice());
                        logger.debug(order);
                        logger.debug(String.format("Realized profit so far: %.8f BTC", counters.getProfit()));
                        break;
                }
        }
    }

    @Subscribe
    public void updatePositions(MarketUpdatedEvent event) {
        this.portfolio.getPositions().entrySet().stream()
            .map(Map.Entry::getValue)
            .forEach(position -> {
                CryptoCurrency crypto = market.getMarket().get(position.getSymbol());
                Float currentPrice = crypto.getLatestPrice();
                Float openPrice = position.getOpenPrice();
                position.setChange(PriceUtil.change(openPrice, currentPrice));
                logger.debug(position);
            });

        eventBus.post(new PositionsUpdatedEvent());
    }

}
