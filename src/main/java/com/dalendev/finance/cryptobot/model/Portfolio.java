package com.dalendev.finance.cryptobot.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daniele.orler
 */
public class Portfolio {

    private final Map<String, Position> portfolio;

    public Portfolio() {
        this.portfolio = new HashMap<>();
    }

    public Map<String, Position> getPortfolio() {
        return portfolio;
    }
}
