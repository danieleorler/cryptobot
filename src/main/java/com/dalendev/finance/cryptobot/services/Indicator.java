package com.dalendev.finance.cryptobot.services;

import com.dalendev.finance.cryptobot.model.CryptoAnalysis;
import com.dalendev.finance.cryptobot.model.CryptoCurrency;
import com.dalendev.finance.cryptobot.model.Position;
import com.dalendev.finance.cryptobot.model.PositionAnalysis;

/**
 * @author daniele.orler
 */
public interface Indicator<C extends CryptoAnalysis, P extends PositionAnalysis> {

    /**
     * Initializes the given CryptoCurrency's analysis
     * @param currency CryptoCurrency to initialize
     */
    void init(CryptoCurrency currency);

    /**
     * Initializes the given Positions's analysis
     * @param position Position to initialize
     */
    void init(Position position);

    /**
     * Adds a new sample to the CryptoCurrency's analysis
     * @param price the new sample
     * @param currency CryptoCurrency to update
     */
    void addSample(Double price, CryptoCurrency currency);

    /**
     * Updates the given Position on market update
     * @param position Position to update
     */
    void updatePosition(Position position);

    /**
     * Decides if the given CryptoCurrency should be bought
     * @param currency
     * @return
     */
    Boolean shouldOpen(CryptoCurrency currency);

    /**
     * Decides if the given position should be closed
     * @param position
     * @return
     */
    Boolean shouldClose(Position position);

    /**
     * Casts generic CryptoAnalysis object to the implementation used by the algorithm
     * @param analysis
     * @return
     */
    default C cast(CryptoAnalysis analysis) {
        return (C) analysis;
    }

    /**
     * Casts generic PositionAnalysis object to the implementation used by the algorithm
     * @param analysis
     * @return
     */
    default P cast(PositionAnalysis analysis) {
        return (P) analysis;
    }

}
