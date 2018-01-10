package com.dalendev.finance.cryptobot.model.events;

import com.dalendev.finance.cryptobot.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daniele.orler
 */
public class OrdersSelectedEvent {

    private final List<Order> orders;

    public OrdersSelectedEvent() {
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }
}
