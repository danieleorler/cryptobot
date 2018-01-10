package com.dalendev.finance.cryptobot.model.exceptions;

/**
 * @author daniele.orler
 */
public class RestRequestException extends RuntimeException {

    public RestRequestException(String message) {
        super(message);
    }
}
