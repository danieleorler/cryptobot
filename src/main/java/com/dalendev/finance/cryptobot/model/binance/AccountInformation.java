package com.dalendev.finance.cryptobot.model.binance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author daniele.orler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInformation {

    private final List<Balance> balances;

    @JsonCreator
    public AccountInformation(@JsonProperty("balances") List<Balance> balances) {
        this.balances = balances;
    }

    public List<Balance> getBalances() {
        return balances;
    }
}
