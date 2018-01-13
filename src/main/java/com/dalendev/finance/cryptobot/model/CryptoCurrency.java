package com.dalendev.finance.cryptobot.model;

import com.google.common.collect.EvictingQueue;

/**
 * @author daniele.orler
 */
public class CryptoCurrency {

    private final String symbol;
    private final EvictingQueue<Double> pricePoints;
    private Double latestPrice;
    private Double change;

    public CryptoCurrency(String symbol) {
        this.symbol = symbol;
        this.change = 0d;
        pricePoints = EvictingQueue.create(30);
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public String getSymbol() {
        return symbol;
    }

    public EvictingQueue<Double> getPricePoints() {
        return pricePoints;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }
}
