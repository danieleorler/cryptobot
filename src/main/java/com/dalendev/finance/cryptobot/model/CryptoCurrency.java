package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class CryptoCurrency {

    private final String symbol;
    private Double latestPrice;

    private CryptoAnalysis analysis;

    public CryptoCurrency(String symbol) {
        this.symbol = symbol;
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

    public void setAnalysis(CryptoAnalysis analysis) {
        this.analysis = analysis;
    }

    public CryptoAnalysis getAnalysis() {
        return analysis;
    }
}
