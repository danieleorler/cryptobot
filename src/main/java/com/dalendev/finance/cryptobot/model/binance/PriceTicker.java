package com.dalendev.finance.cryptobot.model.binance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceTicker {

    private String symbol;
    private Float bidPrice;
    @JsonProperty("bidQty")
    private Float bidQuantity;
    private Float askPrice;
    @JsonProperty("askQty")
    private Float askQuantity;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Float bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Float getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(Float bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public Float getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Float askPrice) {
        this.askPrice = askPrice;
    }

    public Float getAskQuantity() {
        return askQuantity;
    }

    public void setAskQuantity(Float askQuantity) {
        this.askQuantity = askQuantity;
    }
}
