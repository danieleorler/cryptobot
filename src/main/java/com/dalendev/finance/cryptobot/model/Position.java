package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.EvictingQueue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.dalendev.finance.cryptobot.util.PriceUtil.getRealPrice;

/**
 * @author daniele.orler
 */
public class Position {

    private final CryptoCurrency currency;
    private final Double openPrice;
    private final Double closePrice;
    private final Double amount;
    private final LocalDateTime openTime;
    private LocalDateTime closeTime;
    private Double change;
    private PositionAnalysis analysis;


    public Position(CryptoCurrency currency, Order order) {

        this(
                currency,
                order.getPrice(),
                order.getQuantity(),
                LocalDateTime.now()
        );
    }

    public Position(CryptoCurrency currency, Double openPrice, Double amount, LocalDateTime openTime) {
        this.currency = currency;
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
        return currency.getSymbol();
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

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public PositionAnalysis getAnalysis() {
        return analysis;
    }

    public void setAnalysis(PositionAnalysis analysis) {
        this.analysis = analysis;
    }

    @Override
    public String toString() {
        return String.format("%.8f %s opened at price %.8f [%.2f%%] open time %s, %s",
            amount,
            currency.getSymbol(),
            openPrice,
            change,
            openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            analysis.toString());
    }
}
