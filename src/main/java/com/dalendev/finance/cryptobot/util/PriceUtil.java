package com.dalendev.finance.cryptobot.util;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
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

    public static Double adjust(Double amount, Double lot) {
        double reminder = amount % lot;
        return reminder > 0f ? lot - reminder + amount : amount;
    }

    public static Double slope(CryptoCurrency currency) {
        SimpleRegression regression = new SimpleRegression();
        double x = 0;

        for(Double y : currency.getPricePoints()) {
            regression.addData(x++, y);
        }

        return regression.getSlope();
    }

}
