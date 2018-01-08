package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class BTCPrice {

    private Float priceinUSD;

    public BTCPrice() {
        priceinUSD = 0f;
    }

    public Float getPriceinUSD() {
        return priceinUSD;
    }

    public void setPriceinUSD(Float priceinUSD) {
        this.priceinUSD = priceinUSD;
    }
}
