package com.shertech.stockwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lastwalker on 3/8/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHandler";

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    // DB Name
    private static final String DATABASE_NAME = "StockAppDB";
    // DB Table Name
    private static final String TABLE_NAME = "StockWatchTable";
    ///DB Columns
    private static final String SYMBOL = "StockSymbol";
    private static final String COMPANY = "CompanyName";

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    SYMBOL + " TEXT not null unique," +
                    COMPANY + " TEXT not null)";

    private SQLiteDatabase database;

    public void addStock(share stock) {
        Log.d(TAG, "addStock: Adding " + stock.getSymbol());
        ContentValues values = new ContentValues();
        values.put(SYMBOL, stock.getSymbol());
        values.put(COMPANY, stock.getName());
        database.insert(TABLE_NAME, null, values);
    }
    public boolean searchStock(share stock) {
        Log.d(TAG, "searchStock: Searching " + stock.getSymbol());
        Cursor cursor = database.query(
                TABLE_NAME, // The table to query
                new String[]{ SYMBOL, COMPANY }, // The columns to return
                null, // The columns for the WHERE clause, null means “*”
                null, // The values for the WHERE clause, null means “*”
                null, // don't group the rows
                null, // don't filter by row groups
                null);
        if (cursor != null) { // Only proceed if cursor is not null
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0); // 1 st returned column
                if (symbol.equals(stock.getTicker())){
                    return true;
                }else{
                    cursor.moveToNext();
                }

            }
            cursor.close();
        }
        return false;
    }

    public void deleteStock(String symbol) {
        Log.d(TAG, "deleteStock: Deleting Stock " + symbol);
        int cnt = database.delete(
                TABLE_NAME, SYMBOL+" = ?", new String[] { symbol });
        Log.d(TAG, "deleteStock: " + cnt);
    }

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = getWritableDatabase();
        Log.d(TAG, "DatabaseHandler: C'tor DONE");

    }

    public ArrayList<String[]> loadStocks() {
        Log.d(TAG, " loadStocks: Load all symbol-company entries from DB");
        ArrayList<String[]> stocks = new ArrayList<>();
        Cursor cursor = database.query(
                TABLE_NAME, // The table to query
                new String[]{ SYMBOL, COMPANY }, // The columns to return
                null, // The columns for the WHERE clause, null means “*”
                null, // The values for the WHERE clause, null means “*”
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        if (cursor != null) { // Only proceed if cursor is not null
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                String symbol = cursor.getString(0); // 1 st returned column
                String company = cursor.getString(1); // 2 nd returned column
                stocks.add(new String[] {symbol, company});
                cursor.moveToNext();
            }
            cursor.close();
        }
        return stocks;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate is only called is the DB does not exist
        Log.d(TAG, "onCreate: Mking New DB");
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
