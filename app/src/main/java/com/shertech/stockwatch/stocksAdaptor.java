package com.shertech.stockwatch;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lastwalker on 3/2/17.
 */

public class stocksAdaptor extends RecyclerView.Adapter<MyViewHolder> {

    private static final String TAG = "StocksAdapter";
    private List<share> stockList;
    private MainActivity mainAct;

    public stocksAdaptor(List<share> nList, MainActivity ma){
        this.stockList=nList;
        this.mainAct=ma;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW");
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.flaglayout, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        share stock = stockList.get(position);
        if (stock.getPriceChangeAmount()>=0){
        holder.tvA.setText(stock.getSymbol());
            holder.tvA.setTextColor(Color.GREEN);
        holder.tvB.setText(stock.getName());
            holder.tvB.setTextColor(Color.GREEN);
        holder.tvC.setText(stock.getLastTradePrice().toString());
            holder.tvC.setTextColor(Color.GREEN);
        holder.tvD.setText("\u25B2"+stock.pack());
            holder.tvD.setTextColor(Color.GREEN);
             }else{
            holder.tvA.setText(stock.getSymbol());
            holder.tvA.setTextColor(Color.RED);
            holder.tvB.setText(stock.getName());
            holder.tvB.setTextColor(Color.RED);
            holder.tvC.setText(stock.getLastTradePrice().toString());
            holder.tvC.setTextColor(Color.RED);
            holder.tvD.setText("\u25BC"+stock.pack());
            holder.tvD.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
