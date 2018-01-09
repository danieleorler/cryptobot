package com.dalendev.finance.cryptobot.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daniele.orler
 */
public class Market {

    private final Map<String, CryptoCurrency> market;

    public Market() {
        market = new HashMap<>();
    }

    public Map<String, CryptoCurrency> getMarket() {
        return market;
    }
}
