package com.dalendev.finance.cryptobot.model.exceptions;

/**
 * @author daniele.orler
 */
public class LowBalanceException extends RuntimeException {
    public LowBalanceException(String message) {
        super(message);
    }
}
