package com.dalendev.finance.cryptobot.model.binance.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author daniele.orler
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeInfo {

    private List<Symbol> symbols;

    public List<Symbol> getSymbols() {
        return symbols;
    }

    public void setSymbols(List<Symbol> symbols) {
        this.symbols = symbols;
    }
}
