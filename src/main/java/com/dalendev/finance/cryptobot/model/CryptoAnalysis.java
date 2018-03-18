package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;

import java.util.Map;

/**
 * @author daniele.orler
 */
public interface CryptoAnalysis {

    @JsonAnyGetter
    Map<String, Object> toJson();

}
