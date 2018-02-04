package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Position;

/**
 * @author daniele.orler
 */
public interface Indicator {

    /**
     * Adds a new sample to the CryptoCurrency's analysis
     * @param price the new sample
     * @param currency CryptoCurrency to update
     */
    void addSample(Double price, CryptoCurrency currency);

    /**
     * Decides if the given CryptoCurrency should be bought
     * @param currency
     * @return
     */
    Boolean shouldBuy(CryptoCurrency currency);

    /**
     * Decides if the given position should be closed
     * @param position
     * @return
     */
    Boolean shouldSell(Position position);

}
