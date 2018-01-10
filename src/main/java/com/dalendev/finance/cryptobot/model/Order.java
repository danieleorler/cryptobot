package com.dalendev.finance.cryptobot.model;

/**
 * @author daniele.orler
 */
public class Order {

    private final String symbol;
    private final Side side;
    private final Type type;
    private final Float quantity;

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

    public Order(String symbol, Side side, Type type, Float quantity) {
        this.symbol = symbol;
        this.side = side;
        this.type = type;
        this.quantity = quantity;
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
        return String.format("%s %.8f %s with type %s",
                side,
                quantity,
                symbol,
                type);
    }
}
