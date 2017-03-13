package com.shertech.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lastwalker on 3/4/17.
 */

public class AsynLoaderTask1 extends AsyncTask<String,Void,String> {
    private MainActivity mainActivity;
    private static final String TAG = "AsyncCountryLoader1";
    String name,symbol;

    AsynLoaderTask1(MainActivity ma,String symbol){
        mainActivity=ma;
        this.symbol=symbol;
    }
    @Override
    protected String doInBackground(String... params) {
        Uri.Builder builder = new Uri.Builder();
        Log.d(TAG, "onClick: HELLO2");
        builder.scheme("http")
                .authority("finance.google.com")
                .appendPath("finance")
                .appendPath("info")
                .appendQueryParameter("client", "ig")
                .appendQueryParameter("q", symbol);
        String myUrl = builder.build().toString();
        Uri dataUri = Uri.parse(myUrl);
        String urlToUse = dataUri.toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            return null;
        }

        Log.d(TAG, "doInBackground: " + sb.toString());

        return sb.toString();
    }

    private share parseJSON(String s) {

        share msp=new share();
        try {
            String jsonFormattedString = s.replaceAll("//",""); // gives error
            Log.d("Formatted String", jsonFormattedString);
            JSONArray jObjMain = new JSONArray(jsonFormattedString);
            String ticker="";
            Double lastTradePrice=0.0;
            Double priceChangeAmount=0.0;
            Double priceChangePercentage=0.0;


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                ticker = jCountry.getString("t");
                lastTradePrice = jCountry.getDouble("l");

                priceChangeAmount = jCountry.getDouble("c");
                priceChangePercentage = jCountry.getDouble("cp");
                if (ticker.equals(symbol)){
                msp=new share(name, symbol, ticker, lastTradePrice, priceChangeAmount,priceChangePercentage);
                }else
                    msp=null;
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
            msp=null;
        }
        return msp;
    }

    @Override
    protected void onPostExecute(String s) {

        share msp=parseJSON(s);
        if (msp==null){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Stock not found :"+symbol);
            builder.setMessage("Data for stock symbol");
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            mainActivity.setMSP(msp);
        }
    }
}
