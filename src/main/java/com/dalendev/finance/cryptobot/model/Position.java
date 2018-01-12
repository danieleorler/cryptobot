package com.dalendev.finance.cryptobot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Position(Order order) {
        this(order.getSymbol(), order.getPrice(), order.getQuantity(), LocalDateTime.now());
    }

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

    @Override
    public String toString() {
        return String.format("%.8f %s opened at %s at price %.8f current change %.2f%%",
            amount,
            symbol,
            openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            openPrice,
            change);
    }
}
