package com.dalendev.finance.cryptobot.subscribers;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.Position;
import com.dalendev.finance.cryptobot.model.events.MarketUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrderUpdatedEvent;
import com.dalendev.finance.cryptobot.model.events.OrdersSelectedEvent;
import com.dalendev.finance.cryptobot.model.events.PositionsUpdatedEvent;
import com.dalendev.finance.cryptobot.services.ConfigService;
import com.dalendev.finance.cryptobot.services.Indicator;
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

import java.util.Collections;
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
    private final ConfigService configService;
    private final Indicator indicator;

    @Autowired
    public MarkedUpdatedSubscriber(Market market, EventBus eventBus, Portfolio portfolio, Counters counters, OrderAdapter orderAdapter, Exchange exchange, ConfigService configService, Indicator indicator) {
        this.market = market;
        this.portfolio = portfolio;
        this.eventBus = eventBus;
        this.counters = counters;
        this.orderAdapter = orderAdapter;
        this.exchange = exchange;
        this.configService = configService;
        this.indicator = indicator;
        eventBus.register(this);
    }

    @Subscribe
    public void selectBuyOrders(PositionsUpdatedEvent event) {
        if (portfolio.getPositions().size() >= configService.getMaxPositionsOpened())  {
            return;
        }

        CryptoCurrency buyable = this.market.getMarket().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(crypto -> !portfolio.getPositions().containsKey(crypto.getSymbol()))
            .filter(crypto -> !portfolio.getOrders().containsKey(crypto.getSymbol()))
            .filter(indicator::shouldOpen)
            .findFirst()
            .orElse(null);

        if(buyable == null) {
            return;
        }

        Double stepSize = exchange.getLotFilter(buyable.getSymbol()).getStepSize();
        Double quantity = PriceUtil.adjust(configService.getMaxOrderPrice() / buyable.getLatestPrice(), stepSize);
        Order order = new Order.Builder()
            .symbol(buyable.getSymbol())
            .side(Order.Side.BUY)
            .type(Order.Type.MARKET)
            .quantity(quantity)
            .price(buyable.getLatestPrice())
            .status(Order.Status.TO_BE_PLACED)
            .build();

        OrdersSelectedEvent ordersSelectedEvent = new OrdersSelectedEvent();
        ordersSelectedEvent.getOrders().addAll(Collections.singletonList(order));
        eventBus.post(ordersSelectedEvent);
    }

    @Subscribe
    public void selectSellOrders(PositionsUpdatedEvent event) {
        List<Order> orders = portfolio.getPositions().entrySet().stream()
            .map(Map.Entry::getValue)
            .filter(indicator::shouldClose)
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
                    Order orderResult = orderAdapter.placeOrder(order);
                    eventBus.post(new OrderUpdatedEvent(orderResult));
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
                logger.debug(order);
                switch (order.getSide()) {
                    case BUY:
                        portfolio.getOrders().remove(order.getSymbol());
                        CryptoCurrency currency = market.getMarket().get(order.getSymbol());
                        Position openingPosition = new Position(currency, order);
                        indicator.init(openingPosition);
                        portfolio.getPositions().put(order.getSymbol(), openingPosition);
                        break;
                    case SELL:
                        portfolio.getOrders().remove(order.getSymbol());
                        Position closingPosition = portfolio.getPositions().remove(order.getSymbol());
                        counters.addToProfit(closingPosition.getChange());
                        logger.debug(String.format("Realized profit so far: %.8f%%", counters.getProfit()));
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
                Double currentPrice = crypto.getLatestPrice();
                Double openPrice = position.getOpenPrice();
                position.setChange(PriceUtil.change(openPrice, currentPrice));

                indicator.updatePosition(position);

                logger.debug(position);
            });

        eventBus.post(new PositionsUpdatedEvent());
    }

}
