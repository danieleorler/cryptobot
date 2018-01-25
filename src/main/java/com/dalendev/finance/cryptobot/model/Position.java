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
    private Double thresholdMADiff;
    private Double openMADiff;
    @JsonIgnore
    private final EvictingQueue<Double> mADiffs30m;

    public Position(CryptoCurrency currency, Order order) {

        this(
                currency,
                order.getPrice(),
                order.getQuantity(),
                LocalDateTime.now(),
                currency.getAnalysis().getMovingAverageDiff()
        );
    }

    public Position(CryptoCurrency currency, Double openPrice, Double amount, LocalDateTime openTime, Double openMADiff) {
        this.currency = currency;
        this.openPrice = openPrice;
        this.amount = amount;
        this.openTime = openTime;
        this.closePrice = null;
        this.change = 0d;
        this.thresholdMADiff = 0.0;
        this.openMADiff = openMADiff;
        this.mADiffs30m = EvictingQueue.create(30);
        this.mADiffs30m.add(openMADiff);
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

    public Double getThresholdMADiff() {
        return thresholdMADiff;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public EvictingQueue<Double> getMADiffs30m() {
        return mADiffs30m;
    }

    public void setThresholdMADiff(Double mADiffSlope) {
        this.thresholdMADiff = mADiffSlope;
    }

    public Double getOpenMADiff() {
        return openMADiff;
    }

    @Override
    public String toString() {
        return String.format("%.8f %s opened at price %.8f current change %.2f%%, thresholdMA %.8f, currentMA %.8f, open date %s",
            amount,
            currency.getSymbol(),
            openPrice,
            change,
            thresholdMADiff,
            currency.getAnalysis().getMovingAverageDiff(),
            openTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
    }
}
