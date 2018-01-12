package com.dalendev.finance.cryptobot.model.events;

import com.dalendev.finance.cryptobot.model.Order;

/**
 * @author daniele.orler
 */
public class OrderUpdatedEvent {

    private final Order order;

    public OrderUpdatedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
