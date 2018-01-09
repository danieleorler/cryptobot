package com.dalendev.finance.cryptobot.util;

/**
 * @author daniele.orler
 */
public class PriceUtil {

    public static Float change(Float oldestPrice, Float newestPrice) {
        return ((newestPrice - oldestPrice) / newestPrice) * 100;
    }

    public static Float addPercentage(Float price, Float percentage) {
        return price + (price * percentage/100);
    }

}
