package com.dalendev.finance.cryptobot.model.binance;

import java.util.List;

/**
 * @author daniele.orler
 */
public class PriceTickers {

    private List<PriceTicker> tickers;

    public List<PriceTicker> getTickers() {
        return tickers;
    }

    public void setTickers(List<PriceTicker> tickers) {
        this.tickers = tickers;
    }
}
