package com.dalendev.finance.cryptobot.singletons;

/**
 * @author daniele.orler
 */
public class Counters {

    private Double profit;

    public Counters() {
        profit = 0d;
    }

    public Double addToProfit(Double delta) {
        profit += delta;
        return profit;
    }

    public Double getProfit() {
        return profit;
    }
}
