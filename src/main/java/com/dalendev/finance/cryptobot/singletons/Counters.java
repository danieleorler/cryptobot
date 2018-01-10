package com.dalendev.finance.cryptobot.singletons;

/**
 * @author daniele.orler
 */
public class Counters {

    private Float profit;

    public Counters() {
        profit = 0f;
    }

    public Float addToProfit(Float delta) {
        profit += delta;
        return profit;
    }

    public Float getProfit() {
        return profit;
    }
}
