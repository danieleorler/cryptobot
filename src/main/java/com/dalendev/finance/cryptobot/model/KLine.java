package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

/**
 * @author daniele.orler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class KLine {

    @JsonProperty("s")
    String symbol;
    Float open;
    Float close;
    Boolean complete;

    public Boolean isComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Float getOpen() {
        return open;
    }

    public void setOpen(Float open) {
        this.open = open;
    }

    public Float getClose() {
        return close;
    }

    public void setClose(Float close) {
        this.close = close;
    }

    @JsonProperty("k")
    private void unpackNested(Map<String,Object> kline) {
        this.open = Float.parseFloat(kline.get("o").toString());
        this.close = Float.parseFloat(kline.get("c").toString());
        this.complete = Boolean.parseBoolean(kline.get("x").toString());
    }

    @Override
    public String toString() {
        return "KLine{" +
                "symbol='" + symbol + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", change=" + ((close - open) / open) * 100 +"%"+
                '}';
    }
}
