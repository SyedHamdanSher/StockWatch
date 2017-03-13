package com.shertech.stockwatch;

import java.io.Serializable;

/**
 * Created by lastwalker on 3/2/17.
 */

public class share implements Serializable{
    private String name;
    private String symbol;
    private String ticker;
    private Double lastTradePrice;
    private Double priceChangeAmount;
    private Double priceChangePercentage;

    share(){
        name="";
        symbol="";
        ticker="";
        lastTradePrice=0.0;
        priceChangeAmount=0.0;
        priceChangePercentage=0.0;
    }
    share(String name,String symbol,String ticker,Double lastTradePrice,Double priceChangeAmount,Double priceChangePercentage){
        this.name=name;
        this.symbol=symbol;
        this.ticker=ticker;
        this.lastTradePrice=lastTradePrice;
        this.priceChangeAmount=priceChangeAmount;
        this.priceChangePercentage=priceChangePercentage;
    }

    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public Double getLastTradePrice() {
        return lastTradePrice;
    }
    public void setLastTradePrice(Double lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }
    public Double getPriceChangeAmount() {
        return priceChangeAmount;
    }
    public void setPriceChangeAmount(Double priceChangeAmount) {
        this.priceChangeAmount = priceChangeAmount;
    }
    public Double getPriceChangePercentage() {
        return priceChangePercentage;
    }
    public void setPriceChangePercentage(Double priceChangePercentage) {
        this.priceChangePercentage = priceChangePercentage;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String pack() {
        return priceChangeAmount+"("+priceChangePercentage+"%)";
    }
}
