package com.shertech.stockwatch;

import java.io.Serializable;

/**
 * Created by lastwalker on 3/2/17.
 */

public class share1 implements Serializable {
    private String ticker;
    private String lastTradePrice;
    private String priceChangeAmount;
    private String priceChangePercentage;

    share1(){
        ticker="";
        lastTradePrice="";
        priceChangeAmount="";
        priceChangePercentage="";
    }

    public String getTicker() {
        return ticker;
    }
    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
    public String getLastTradePrice() {
        return lastTradePrice;
    }
    public void setLastTradePrice(String lastTradePrice) {
        this.lastTradePrice = lastTradePrice;
    }
    public String getPriceChangeAmount() {
        return priceChangeAmount;
    }
    public void setPriceChangeAmount(String priceChangeAmount) {
        this.priceChangeAmount = priceChangeAmount;
    }
    public String getPriceChangePercentage() {
        return priceChangePercentage;
    }
    public void setPriceChangePercentage(String priceChangePercentage) {
        this.priceChangePercentage = priceChangePercentage;
    }
}
