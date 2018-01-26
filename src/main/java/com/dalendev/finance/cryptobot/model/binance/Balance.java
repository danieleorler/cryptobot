package com.dalendev.finance.cryptobot.model.binance;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author daniele.orler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {

    private final String asset;
    private final Double free;
    private final Double locked;

    @JsonCreator
    public Balance(
            @JsonProperty("asset") String asset,
            @JsonProperty("free") Double free,
            @JsonProperty("locked") Double locked) {
        this.asset = asset;
        this.free = free;
        this.locked = locked;
    }

    public String getAsset() {
        return asset;
    }

    public Double getFree() {
        return free;
    }

    public Double getLocked() {
        return locked;
    }
}
