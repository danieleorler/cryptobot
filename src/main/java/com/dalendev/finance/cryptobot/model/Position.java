package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class Position {

    private final String symbol;
    private final Float openPrice;
    private final Float closePrice;
    private final Float amount;
    private Float change;

    public Position(String symbol, Float openPrice, Float amount) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.amount = amount;
        this.closePrice = null;
        this.change = 0f;
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

    public Float getOpenPrice() {
        return openPrice;
    }

    public Float getClosePrice() {
        return closePrice;
    }

    public Float getAmount() {
        return amount;
    }
}
