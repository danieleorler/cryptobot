package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.EvictingQueue;

/**
 * @author daniele.orler
 */
public class Analysis {

    private final EvictingQueue<Double> prices24;
    @JsonIgnore
    private final EvictingQueue<Double> prices6;
    private final EvictingQueue<Double> EMA24h;
    private final EvictingQueue<Double> EMA6h;
    private final EvictingQueue<Double> price30m;

    private Double price30mSlope = 0.0;
    private Double movingAverageDiff = 0.0;

    public Analysis() {
        EMA6h = EvictingQueue.create(24 * 60);
        EMA24h = EvictingQueue.create(24 * 60);
        prices6 = EvictingQueue.create(6 * 60);
        prices24 = EvictingQueue.create(24 * 60);
        price30m = EvictingQueue.create(30);
    }

    public EvictingQueue<Double> getEMA24h() {
        return EMA24h;
    }

    public EvictingQueue<Double> getEMA6h() {
        return EMA6h;
    }

    public EvictingQueue<Double> getPrice30m() {
        return price30m;
    }

    public Double getPrice30mSlope() {
        return price30mSlope;
    }

    public Double getMovingAverageDiff() {
        return movingAverageDiff;
    }

    public EvictingQueue<Double> getPrices24() {
        return prices24;
    }

    public EvictingQueue<Double> getPrices6() {
        return prices6;
    }

    public void setMovingAverageDiff(Double movingAverageDiff) {
        this.movingAverageDiff = movingAverageDiff;
    }

    public void setPrice30mSlope(Double price30mSlope) {
        this.price30mSlope = price30mSlope;
    }
}
