package com.dalendev.finance.cryptobot.singletons;

import com.dalendev.finance.cryptobot.model.binance.exchange.ExchangeInfo;
import com.dalendev.finance.cryptobot.model.binance.exchange.Filters;
import com.dalendev.finance.cryptobot.model.exceptions.SymbolFilterNotFoundException;

/**
 * @author daniele.orler
 */
public class Exchange {

    private ExchangeInfo exchangeInfo;

    public ExchangeInfo getExchangeInfo() {
        return exchangeInfo;
    }

    public void setExchangeInfo(ExchangeInfo exchangeInfo) {
        this.exchangeInfo = exchangeInfo;
    }

    public Filters.LotFilter getLotFilter(String symbol) {
        Filters.Filter filter = exchangeInfo.getSymbols().stream()
                .filter(s -> s.getSymbol().equals(symbol))
                .flatMap(s -> s.getFilters().stream())
                .filter(f -> f.getFilterType() == Filters.FilterType.LOT_SIZE)
                .findFirst()
                .orElseThrow(() -> new SymbolFilterNotFoundException(""));

        return (Filters.LotFilter)filter;
    }
}
