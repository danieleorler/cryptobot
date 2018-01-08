package com.dalendev.finance.cryptobot.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daniele.orler
 */
public class MarketsStatus {

    private final Map<Symbol, Float> marketChange;


    public MarketsStatus() {
        marketChange = new HashMap<>();
    }

    public void updateMarketChange(Symbol symbol, Float change) {
        if(marketChange.containsKey(symbol)) {
            marketChange.put(symbol, marketChange.get(symbol) + change);
        } else {
            marketChange.put(symbol, change);
        }
    }
}
