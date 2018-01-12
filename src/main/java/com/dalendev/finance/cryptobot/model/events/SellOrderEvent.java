package com.dalendev.finance.cryptobot.model.events;

import com.dalendev.finance.cryptobot.model.Order;

/**
 * @author daniele.orler
 */
public class SellOrderEvent {

    private final Order order;

    public SellOrderEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
