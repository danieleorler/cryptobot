package com.dalendev.finance.cryptobot.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author daniele.orler
 */
public class Position {

    private final String symbol;
    private final Double openPrice;
    private final Double closePrice;
    private final Double amount;
    private final LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Double change;

    public Position(Order order) {
        this(order.getSymbol(), order.getPrice(), order.getQuantity(), LocalDateTime.now());
    }

    public Position(String symbol, Double openPrice, Double amount, LocalDateTime openTime) {
        this.symbol = symbol;
        this.openPrice = openPrice;
        this.amount = amount;
        this.openTime = openTime;
        this.closePrice = null;
        this.change = 0d;
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

    public Double getOpenPrice() {
        return openPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public Double getAmount() {
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
