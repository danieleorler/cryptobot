package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class CryptoCurrency {

    private final String symbol;
    private Double latestPrice;

    private final Analysis analysis;

    public CryptoCurrency(String symbol) {
        this.symbol = symbol;
        analysis = new Analysis();
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public void addPriceSample(double price) {
        analysis.addPrice(price);
    }

    public Analysis getAnalysis() {
        return analysis;
    }
}
