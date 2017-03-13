package com.shertech.stockwatch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {
    share msp=new share(),msp1=new share();
    private static final int B_REQ = 1;
    private List<share> stockList = new ArrayList<>();
    private List<share> stockListT = new ArrayList<>();// Main content is here
    private RecyclerView recyclerView; // Layout's recyclerview
    private stocksAdaptor nAdapter; // Data to recyclerview adapter
    private static final String TAG = "mainActivity";
    private SwipeRefreshLayout swiper;
    DatabaseHandler databaseHandler;
    private static final String SEARCH = "http://www.marketwatch.com/investing/stock/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        nAdapter = new stocksAdaptor(stockList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swiper = (SwipeRefreshLayout) findViewById(R.id.swiper);
        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });
        msp1=new share();
        databaseHandler = new DatabaseHandler(this);
        if(networkCheck()){
        ArrayList<String[]> xy=databaseHandler.loadStocks();
            if(xy.size()!=0){
                for (String[] a:xy){
                    new AsynLoaderTask1(this,a[0]).execute();
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.wel));
                builder.setMessage(getString(R.string.welm)+"\n"+getString(R.string.welm1));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.no_I));
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage(getString(R.string.no_Im));
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.btAdd:
                if(networkCheck()){
                    clickS();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle(getString(R.string.no_I));
                    builder.setIcon(R.drawable.ic_warning_black_24dp);
                    builder.setMessage(getString(R.string.no_Im));
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void setMSP(share s){
        if(databaseHandler.searchStock(s)){
            ArrayList<String[]> xy=databaseHandler.loadStocks();
            msp1=new share();
            Log.d(TAG, "setMSP5: "+s.getTicker());
            if(xy.size()!=0){
                for (String[] a:xy){
                    if (a[0].equals(s.getTicker())){
                        msp1.setName(a[1]);
                        msp1.setSymbol(s.getTicker());
                        msp1.setTicker(s.getTicker());
                        msp1.setLastTradePrice(s.getLastTradePrice());
                        msp1.setPriceChangeAmount(s.getPriceChangeAmount());
                        msp1.setPriceChangePercentage(s.getPriceChangePercentage());
                        stockList.add(msp1);
                    }
                }
               Collections.sort(stockList, stockComparator);
                nAdapter.notifyDataSetChanged();
                Log.d(TAG, "setMSP1: "+stockList);
            }
        }else{
            stockList.clear();
            ArrayList<String[]> xy=databaseHandler.loadStocks();
            if(xy.size()!=0){
                msp1=new share();
                for (String[] a:xy){
                    new AsynLoaderTask1(this,a[0]).execute();}
            }
            Log.d(TAG, "setMSP2: "+stockList);
            msp.setTicker(s.getTicker());
            msp.setLastTradePrice(s.getLastTradePrice());
            msp.setPriceChangeAmount(s.getPriceChangeAmount());
            msp.setPriceChangePercentage(s.getPriceChangePercentage());
            stockList.add(msp);
            Log.d(TAG, "setMSP3: "+stockList);
            Collections.sort(stockList, stockComparator);
            Log.d(TAG, "setMS4P: "+stockList);
            databaseHandler.addStock(msp);
            recyclerView.setAdapter(nAdapter);
        }
    }


    @Override
    public void onClick(View v) {
        Toast.makeText(this,R.string.pros,Toast.LENGTH_SHORT).show();
        int pos = recyclerView.getChildLayoutPosition(v);
        share c = stockList.get(pos);
        String url = SEARCH+c.getSymbol();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public boolean onLongClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        cancel(pos);
        return true;
    }
    public void clickS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View v=inflater.inflate(R.layout.addstock, null);
        builder.setTitle(getString(R.string.addS1));
        builder.setMessage(getString(R.string.addS));
        builder.setView(v);

        builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                call(v);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void cancel(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete stock '"+stockList.get(pos).getSymbol()+"'?");
        builder.setIcon(R.drawable.ic_delete_forever_black_24dp);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(stockList.get(pos).getSymbol());
                stockList.remove(pos);
                recyclerView.setAdapter(nAdapter);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void doRefresh() {
        if(networkCheck()){
            ArrayList<String[]> xy=databaseHandler.loadStocks();
            if(xy.size()!=0){
                for (String[] a:xy){
                    new AsynLoaderTask1(this,a[0]).execute();
                }
            }
            stockList.clear();
        }
        else{AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.no_I));
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage(getString(R.string.no_Im));
            AlertDialog dialog = builder.create();
            dialog.show();}
        swiper.setRefreshing(false);
    }

    public boolean networkCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    void call(View view){
        EditText E=(EditText) view.findViewById(R.id.etAdd);
        share flag=new share();
        flag.setSymbol(E.getText().toString());
        flag.setTicker(E.getText().toString());
        if(databaseHandler.searchStock(flag)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.dup));
            builder.setIcon(R.drawable.ic_warning_black_24dp);
            builder.setMessage("Stock Symbol "+flag.getSymbol()+" is already displayed");
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            new AsyncLoaderTask(this,E.getText().toString()).execute();
        }
    }
    public void processNewStock(String s,String n){
        msp.setName(n);
        msp.setSymbol(s);

    }

    public static Comparator<share> stockComparator = new Comparator<share>() {

        @Override
        public int compare(share o1, share o2) {
            String start = null,end=null;
            try {
                start = o1.getSymbol();
                end = o2.getSymbol();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "stockComparator: " + o1.getSymbol()+start+end);

            return start.compareTo(end);
        }

    };
}
