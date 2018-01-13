package com.dalendev.finance.cryptobot.jobs;

import com.dalendev.finance.cryptobot.adapters.rest.binance.OrderAdapter;
import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.events.OrderUpdatedEvent;
import com.dalendev.finance.cryptobot.singletons.Portfolio;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author daniele.orler
 */
@Component
public class CheckOrderJob {

    private final EventBus eventBus;
    private final Portfolio portfolio;
    private final OrderAdapter adapter;

    @Autowired
    public CheckOrderJob(EventBus eventBus, Portfolio portfolio, OrderAdapter adapter) {
        this.eventBus = eventBus;
        this.portfolio = portfolio;
        this.adapter = adapter;
    }

    @Scheduled(fixedDelay = 30000)
    public void checkOrders() {
        portfolio.getOrders().entrySet().stream()
            .map(Map.Entry::getValue)
            .forEach(o -> {
                Order order = null;
                try {
                    order = adapter.checkOrder(o);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                o.setStatus(order.getStatus());
                //o.setStatus(Order.Status.FILLED);
                eventBus.post(new OrderUpdatedEvent(o));
            });
    }
}
