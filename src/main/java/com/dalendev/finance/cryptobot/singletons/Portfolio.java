package com.dalendev.finance.cryptobot.singletons;

import com.dalendev.finance.cryptobot.model.Order;
import com.dalendev.finance.cryptobot.model.Position;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daniele.orler
 */
public class Portfolio {

    private final Map<String, Position> positions;
    private final Map<String, Order> orders;

    public Portfolio() {
        this.positions = new ConcurrentHashMap<>();
        this.orders = new ConcurrentHashMap<>();
    }

    public Map<String, Position> getPositions() {
        return positions;
    }

    public Map<String, Order> getOrders() {
        return orders;
    }
}
