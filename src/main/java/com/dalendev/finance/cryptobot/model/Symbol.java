package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public enum Symbol {
    ETHBTC("ethbtc", false),
    ADABTC("adabtc", false),
    XVGBTC("xvgbtc", false),
    BTCUSDT("btcusdt", true);

    private final String topic;
    private final Boolean basePrice;

    Symbol(String topic, Boolean basePrice) {
        this.topic = topic;
        this.basePrice = basePrice;
    }

    public String getTopic() {
        return topic;
    }

    public Boolean isBasePrice() {
        return basePrice;
    }
}
