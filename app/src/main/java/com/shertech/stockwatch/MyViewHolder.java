package com.shertech.stockwatch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by lastwalker on 3/2/17.
 */

public class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView tvA;
    public TextView tvB;
    public TextView tvC;
    public TextView tvD;
    public MyViewHolder(View itemView) {
        super(itemView);
        tvA = (TextView) itemView.findViewById(R.id.tvA);
        tvB = (TextView) itemView.findViewById(R.id.tvB);
        tvC = (TextView) itemView.findViewById(R.id.tvC);
        tvD = (TextView) itemView.findViewById(R.id.tvD);
    }
}
