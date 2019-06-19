package com.susu.inventory_management_susu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Iterator;
import java.util.Map;

import androidx.annotation.Nullable;

public class InventoryDB extends SQLiteOpenHelper {

    private static String DB_NAME = "inventory";
    private static String inventory_table = "inventory_data";
    private static String transaction_table = "transaction_table";
    private static String  MEASURE_TABLE = "measurement";
    public InventoryDB(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // CREATING TABLE TO STORE INVENTORY ITEMS
        db.execSQL("CREATE TABLE IF NOT EXISTS "+inventory_table+"(" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " item_name TEXT," +
                " item_barcode TEXT," +
                " item_quantity TEXT," +
                " item_price TEXT," +
                " is_soled TEXT," +
                " date_added TEXT)"
        );

        db.execSQL("CREATE TABLE IF NOT EXISTS "+ MEASURE_TABLE+ " (" +
                "unit_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "unit_name TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "+transaction_table+"(" +
                "item_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                " item_name TEXT," +
                " item_barcode TEXT," +
                " item_quantity INTEGER," +
                " item_price TEXT," +
                " date_sold TEXT)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+inventory_table);
        onCreate(db);
    }

    public boolean insertRecord(String item_name, String item_price, String item_barcode,String quantity, String is_soled, String date_added){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("item_name", item_name);
        contentValues.put("item_price", item_price);
        contentValues.put("item_barcode", item_barcode);
        contentValues.put("item_quantity", quantity);
        contentValues.put("is_soled", is_soled);
        contentValues.put("date_added", date_added );

//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+inventory_table);
//        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+transaction_table);

        long result = sqLiteDatabase.insert(inventory_table, null, contentValues );

        return result != -1;
    }

    public void update(int id, int quantity){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE "+inventory_table+" SET item_quantity = "+quantity+" WHERE item_id ="+id);
    }

    public boolean insertTransaction(Map<String, String> values){

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Iterator it = values.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            contentValues.put(pair.getKey().toString(), pair.getValue().toString());
            it.remove(); // avoids a ConcurrentModificationException
        }

        long result = sqLiteDatabase.insert(transaction_table, null, contentValues );

        return result != -1;
    }

    public boolean insertUnit(String unit_name){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("unit_name", unit_name);

        long result = sqLiteDatabase.insert(MEASURE_TABLE, null, contentValues );
        return result != -1;
    }

    public Cursor getAllData(String table, int isSold){
        SQLiteDatabase database = this.getWritableDatabase();
        onCreate(database);
        return database.rawQuery("SELECT * FROM "+table+" WHERE is_soled = "+isSold+" ORDER BY item_id DESC " , null) ;
    }

    public Cursor getAllData(String table){
        SQLiteDatabase database = this.getWritableDatabase();
        onCreate(database);
        return database.rawQuery("SELECT * FROM "+table+" ORDER BY item_id DESC " , null) ;
    }

    public Cursor getAllUnit(){
        SQLiteDatabase database = this.getWritableDatabase();
        onCreate(database);
        return database.rawQuery("SELECT * FROM "+MEASURE_TABLE , null) ;
    }

}
