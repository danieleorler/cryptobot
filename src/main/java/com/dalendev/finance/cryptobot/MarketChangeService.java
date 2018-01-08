package com.dalendev.finance.cryptobot;

import com.dalendev.finance.cryptobot.model.MarketsStatus;
import com.dalendev.finance.cryptobot.model.Symbol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author daniele.orler
 */

@Service
public class MarketChangeService {

    private final MarketsStatus marketsStatus;

    @Autowired
    public MarketChangeService(MarketsStatus marketsStatus) {
        this.marketsStatus = marketsStatus;
    }

    public void updateMarket(Symbol symbol, Float change) {
        this.marketsStatus.updateMarketChange(symbol, change);
    }
}
