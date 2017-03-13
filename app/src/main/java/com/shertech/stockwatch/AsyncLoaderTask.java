package com.shertech.stockwatch;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lastwalker on 3/2/17.
 */

public class AsyncLoaderTask extends AsyncTask<String,Void,String> {

    private MainActivity mainActivity;
    String[] a;
    private static final String API_KEY = "8fb671fe8ea0015ece0d8aca622399850dfce526";
    private static final String TAG = "AsyncStockLoader";
    String symbol;
    DatabaseHandler db;

    AsyncLoaderTask(MainActivity ma,String symbol) {
        mainActivity = ma;
        this.symbol=symbol;
    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(mainActivity, "Loading Stock Data...", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String... params) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("stocksearchapi.com")
                .appendPath("api")
                .appendPath("")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("search_text", symbol);
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

    private List<share> parseJSON(String s) {
        List<share> stockList1=new ArrayList<>();
        share msp;
        try {
            JSONArray jObjMain = new JSONArray(s);
            String name="";
            String symbol="";
            String ticker="";
            Double lastTradePrice=0.0;
            Double priceChangeAmount=0.0;
            Double priceChangePercentage=0.0;


            for (int i = 0; i < jObjMain.length(); i++) {
                JSONObject jCountry = (JSONObject) jObjMain.get(i);
                name = jCountry.getString("company_name");
                symbol = jCountry.getString("company_symbol");
                msp=new share(name, symbol, ticker, lastTradePrice, priceChangeAmount,priceChangePercentage);
                stockList1.add(msp);
            }
        } catch (Exception e) {
            Log.d(TAG, "parseJSON: " + e.getMessage());
            e.printStackTrace();
        }
        return stockList1;
    }



    @Override
    protected void onPostExecute(String s) {
        List<share> msp=parseJSON(s);
        if (msp.size()==0){
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle("Symbol not found :"+symbol);
            builder.setMessage("Data for stock symbol");
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if (msp.size()==1){
            mainActivity.processNewStock(msp.get(0).getSymbol(),msp.get(0).getName());
            new AsynLoaderTask1(mainActivity,msp.get(0).getSymbol()).execute();
        }else{
            a=new String[msp.size()];
            for (int i=0;i<msp.size();i++){
                a[i]=msp.get(i).getSymbol()+"--"+msp.get(i).getName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
            builder.setTitle(mainActivity.getString(R.string.addS2));

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
                    builder.setItems(a, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] S=a[which].split("--");
                            db=new DatabaseHandler(mainActivity);
                            if(db.searchStock(new share(S[1],S[0],S[0],0.0,0.0,0.0))){
                                Log.d(TAG, "onClick: HELLOEE");
                                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                                builder.setTitle(mainActivity.getString(R.string.dup));
                                builder.setIcon(R.drawable.ic_warning_black_24dp);
                                builder.setMessage("Stock Symbol "+S[0]+" is already displayed");
                                AlertDialog dialogs = builder.create();
                                dialogs.show();
                            }else{
                            mainActivity.processNewStock(S[0],S[1]);

                            new AsynLoaderTask1(mainActivity,S[0]).execute();}

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
