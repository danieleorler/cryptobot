package com.dalendev.finance.cryptobot.model;

import com.dalendev.finance.cryptobot.util.PriceUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.EvictingQueue;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

/**
 * @author daniele.orler
 */
public class CryptoCurrency {

    private final String symbol;
    private final EvictingQueue<Double> pricePoints;
    private Double maDiff = 0.0;
    @JsonIgnore
    private final EvictingQueue<Double> price30m;
    private Double price30mSlope = 0.0;
    private Double latestPrice;
    private Double change;
    @JsonIgnore
    public final DescriptiveStatistics movingAverage24h;
    @JsonIgnore
    public final DescriptiveStatistics movingAverage6h;

    public CryptoCurrency(String symbol) {
        this.symbol = symbol;
        this.change = 0d;
        pricePoints = EvictingQueue.create(30);
        movingAverage6h = new DescriptiveStatistics(6 * 60);
        movingAverage24h = new DescriptiveStatistics(24 * 60);
        price30m = EvictingQueue.create(30);
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

    public EvictingQueue<Double> getPricePoints() {
        return pricePoints;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public void addPriceSample(double price) {
        movingAverage6h.addValue(price);
        movingAverage24h.addValue(price);

        if(movingAverage24h.getValues().length >= movingAverage24h.getWindowSize()) {
            Double diff = movingAverage6h.getMean() - movingAverage24h.getMean();
            this.maDiff = PriceUtil.getPercentage(movingAverage6h.getMean(), diff);
            this.price30m.add(price);
            this.price30mSlope = PriceUtil.slope(this.price30m);
        }
    }

    public Double getMADiff() {
        return maDiff;
    }

    public Double getPrice30mSlope() {
        return price30mSlope;
    }
}
