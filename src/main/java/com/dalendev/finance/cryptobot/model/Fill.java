package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author daniele.orler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fill {

    private Double price;
    @JsonProperty("qty")
    private Double quantity;
    private Double commission;
    private String commissionAsset;

    public Fill() {
    }

    public Fill(Double price, Double quantity) {
        this.price = price;
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public String getCommissionAsset() {
        return commissionAsset;
    }

    public void setCommissionAsset(String commissionAsset) {
        this.commissionAsset = commissionAsset;
    }
}
