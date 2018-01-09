package com.dalendev.finance.cryptobot.model;

import java.time.LocalDateTime;

/**
 * @author daniele.orler
 */
public class Position {

    private final String symbol;
    private final Float openPrice;
    private final Float closePrice;
    private final Float amount;
    private final LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Float change;

    public Position(String symbol, Float openPrice, Float amount, LocalDateTime openTime) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.amount = amount;
        this.openTime = openTime;
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

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public Float getAmount() {
        return amount;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }
}
