package com.dalendev.finance.cryptobot.model.exceptions;

/**
 * @author daniele.orler
 */
public class SymbolFilterNotFoundException extends RuntimeException {

    public SymbolFilterNotFoundException(String message) {
        super(message);
    }
}
