package com.dalendev.finance.cryptobot.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author daniele.orler
 */
public class Order {

    private final String symbol;
    private final Side side;
    private final Type type;
    private final Float quantity;
    private final Float price;
    private Long id;
    private Status status;

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
            @JsonProperty("executedQty") Float quantity,
            @JsonProperty("price") Float price) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
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

    public Float getQuantity() {
        return quantity;
    }

    public Float getPrice() {
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
        private Float quantity;
        private Float price;
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

        public Builder quantity(Float quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder price(Float price) {
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
