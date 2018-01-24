package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author daniele.orler
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private final String symbol;
    private final Side side;
    private final Type type;
    private final Double quantity;
    @JsonProperty("executedPrice")
    private final Double price;
    @JsonProperty("orderId")
    private Long id;
    private Status status;
    private List<Fill> fills;

    public enum Status {
        TO_BE_PLACED,
        PLACED,
        NEW,
        PARTIALLY_FILLED,
        FILLED,
        CANCELED,
        PENDING_CANCEL,
        REJECTED,
        EXPIRED
    }

    public enum Side {
        BUY,
        SELL
    }

    public enum Type {
        LIMIT,
        MARKET,
        STOP_LOSS,
        STOP_LOSS_LIMIT,
        TAKE_PROFIT,
        TAKE_PROFIT_LIMIT,
        LIMIT_MAKER,
    }

    public enum TimeInForce {
        GTC,
        IOC,
        FOK
    }

    @JsonCreator
    public Order(
            @JsonProperty("symbol") String symbol,
            @JsonProperty("side") Side side,
            @JsonProperty("type") Type type,
            @JsonProperty("executedQty") Double quantity,
            @JsonProperty("executedPrice") Double price) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.fills = new ArrayList<>();
    }

    public String getSymbol() {
        return symbol;
    }

    public Side getSide() {
        return side;
    }

    public Type getType() {
        return type;
    }

    public Double getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Fill> getFills() {
        return fills;
    }

    public void setFills(List<Fill> fills) {
        this.fills = fills;
    }

    @Override
    public String toString() {
        return String.format("%s %s %.8f %s at price %.8f with type %s",
                status,
                side,
                quantity,
                symbol,
                price,
                type);
    }

    public static class Builder {
        private String symbol;
        private Side side;
        private Type type;
        private Double quantity;
        private Double price;
        private Long id;
        private Status status;

        public Builder symbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        public Builder side(Side side) {
            this.side = side;
            return this;
        }

        public Builder type(Type type) {
            this.type = type;
            return this;
        }

        public Builder quantity(Double quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder id(Integer id) {
            this.id = new Long(id);
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Order build() {
            Order order = new Order(symbol, side, type, quantity, price);
            order.setId(id);
            order.setStatus(status);
            return order;
        }
    }
}
