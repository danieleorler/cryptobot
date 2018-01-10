package com.dalendev.finance.cryptobot.model.binance.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * @author daniele.orler
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Symbol {

    private String symbol;
    private List<Filters.Filter> filters;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public List<Filters.Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filters.Filter> filters) {
        this.filters = filters;
    }
}
