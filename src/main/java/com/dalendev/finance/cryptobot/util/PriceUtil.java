package com.dalendev.finance.cryptobot.util;

import com.google.common.collect.EvictingQueue;
import org.apache.commons.math3.stat.regression.SimpleRegression;

/**
 * @author daniele.orler
 */
public class PriceUtil {

    public static Double change(Double oldestPrice, Double newestPrice) {
        return ((newestPrice - oldestPrice) / newestPrice) * 100;
    }

    public static Double addPercentage(Double price, Double percentage) {
        return price + (price * percentage/100);
    }

    public static Double getPercentage(Double price, Double amount) {
        return (amount / price) * 100;
    }

    public static Double adjust(Double amount, Double lot) {
        double reminder = amount % lot;
        return reminder > 0f ? lot - reminder + amount : amount;
    }

    public static Double slope(EvictingQueue<Double> dataPoints) {
        SimpleRegression regression = new SimpleRegression();
        double x = 0;

        for(Double y : dataPoints) {
            regression.addData(x++, y);
        }

        return regression.getSlope();
    }

}
