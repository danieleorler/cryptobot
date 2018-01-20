package com.dalendev.finance.cryptobot.singletons;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daniele.orler
 */
public class Market {

    private final Map<String, CryptoCurrency> market;
    private boolean marketReady;

    public Market() {
        market = new ConcurrentHashMap<>();
        marketReady = false;
    }

    public Map<String, CryptoCurrency> getMarket() {
        return market;
    }

    public boolean isMarketReady() {
        return marketReady;
    }

    public void setMarketReady(boolean marketReady) {
        this.marketReady = marketReady;
    }
}
