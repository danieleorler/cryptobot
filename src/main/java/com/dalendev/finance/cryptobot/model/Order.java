package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class Order {

    private final String symbol;
    private final Side side;
    private final Type type;
    private final Float quantity;
    private final Float price;

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

    public Order(String symbol, Side side, Type type, Float quantity, Float price) {
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

    @Override
    public String toString() {
        return String.format("%s %.8f %s at price %.8f with type %s",
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

        public Order build() {
            return new Order(symbol,side, type, quantity, price);
        }
    }
}
