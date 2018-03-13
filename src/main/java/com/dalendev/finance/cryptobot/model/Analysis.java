package com.dalendev.finance.cryptobot.model;

import com.google.common.collect.EvictingQueue;

import java.util.HashMap;
import java.util.Map;

/**
 * @author daniele.orler
 */
public class Analysis {

    private final EvictingQueue<Double> prices24;
    private final EvictingQueue<Double> EMA24h;
    private final EvictingQueue<Double> EMA6h;

    private Double movingAverageDiff = 0.0;

    private final Map<String, Object> extraProperties;

    public Analysis() {
        EMA6h = EvictingQueue.create(24 * 60);
        EMA24h = EvictingQueue.create(24 * 60);
        prices24 = EvictingQueue.create(24 * 60);
        extraProperties = new HashMap<>();
    }

    public EvictingQueue<Double> getEMA24h() {
        return EMA24h;
    }

    public EvictingQueue<Double> getEMA6h() {
        return EMA6h;
    }

    public Double getMovingAverageDiff() {
        return movingAverageDiff;
    }

    public EvictingQueue<Double> getPrices24() {
        return prices24;
    }

    public void setMovingAverageDiff(Double movingAverageDiff) {
        this.movingAverageDiff = movingAverageDiff;
    }

    public Map<String, Object> getExtraProperties() {
        return extraProperties;
    }
}
