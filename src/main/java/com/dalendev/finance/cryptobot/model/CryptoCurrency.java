package com.dalendev.finance.cryptobot.model;

import com.google.common.collect.EvictingQueue;

/**
 * @author daniele.orler
 */
public class CryptoCurrency {

    private final String symbol;
    private final EvictingQueue<Float> pricePoints;
    private Float latestPrice;
    private Float change;

    public CryptoCurrency(String symbol) {
        this.symbol = symbol;
        this.change = 0f;
        pricePoints = EvictingQueue.create(30);
    }

    public Float getChange() {
        return change;
    }

    public void setChange(Float change) {
        this.change = change;
    }

    public String getSymbol() {
        return symbol;
    }

    public EvictingQueue<Float> getPricePoints() {
        return pricePoints;
    }

    public Float getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Float latestPrice) {
        this.latestPrice = latestPrice;
    }
}
