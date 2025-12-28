package com.plataformtrade.domain;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Order {
    private final String orderId;
    private final String accountId;
    private final String marketId;
    private final String side;
    private final int quantity;
    private final int price;
    private int fillQuantity;
    private int fillPrice;
    private String status;
    private final Date timestamp;

    private Order(String orderId, String accountId, String marketId, String side, int quantity, int price, int fillPrice, int fillQuantity, String status, Date timestamp) {
        this.orderId = orderId;
        this.accountId = accountId;
        this.marketId = marketId;
        this.side = side;
        this.quantity = quantity;
        this.price = price;
        this.fillPrice = fillPrice;
        this.fillQuantity = fillQuantity;
        this.status = status;
        this.timestamp = new Date(timestamp.getTime());
    }

    public static Order create(String accountId, String marketId, String side, int quantity, int price){
        validateCreateParameters(accountId, marketId, side, quantity, price);
        var orderId = UUID.randomUUID().toString();
        var status = "open";
        var timestamp = new Date();
        var fillQuantity = 0;
        var fillPrice = 0;
        return new Order(orderId, accountId, marketId, side, quantity, price, fillPrice, fillQuantity, status, timestamp);
    }

    public int getAvailableQuantity(){
        return this.quantity - this.fillQuantity;
    }

    public void fill(int quantity, int price){
        this.fillPrice = ((this.fillQuantity * this.fillPrice) + (quantity * price)) / (this.fillQuantity + quantity);
        this.fillQuantity += quantity;
        if (this.getAvailableQuantity() == 0){
            this.status = "closed";
        }
    }

    public void calculateStatus(){
        if(this.getAvailableQuantity() == 0){
            this.status = "closed";
        }
    }

    private static void validateCreateParameters(String accountId, String marketId,
                                                 String side, int quantity, int price) {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        Objects.requireNonNull(marketId, "marketId cannot be null");
        Objects.requireNonNull(side, "side cannot be null");

        if (!side.equals("buy") && !side.equals("sell")) {
            throw new IllegalArgumentException("side must be 'buy' or 'sell'");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
    }

    public Date getTimestamp() {
        return new Date(timestamp.getTime());
    }

    public String getStatus() {
        return status;
    }

    public int getFillPrice() {
        return fillPrice;
    }

    public int getFillQuantity() {
        return fillQuantity;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSide() {
        return side;
    }

    public String getMarketId() {
        return marketId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getOrderId() {
        return orderId;
    }
}
