package com.sdcode.cryptotracker.models;


public class CoinListModelClass {
    private String image, symbol, name, id;
    private double current_price;

    public CoinListModelClass(String id, String image, String symbol, String name, double current_price) {
        this.id = id;
        this.image = image;
        this.symbol = symbol;
        this.name = name;
        this.current_price = current_price;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getCurrent_price() {
        return current_price;
    }
}
