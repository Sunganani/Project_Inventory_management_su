package com.susu.inventory_management_susu.helpers;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.susu.inventory_management_susu.Item_quantity_dialog;
import com.susu.inventory_management_susu.R;
import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.pending_transaction_item;
import com.susu.inventory_management_susu.transaction_item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class support_methods {
    public static RecyclerView.Adapter transaction_adapter;

    public static RecyclerView.Adapter pending_transaction_adapter;

    public static List<pending_transaction_item> pendingTransactionItemList;

    public static ArrayList<transaction_item> transactionItems;

    public static ArrayList<pending_transaction_item> pending_transaction_items = new ArrayList<>();

    public static Map<String, pending_transaction_item> transactionListItem = new HashMap<>();

    public static Map<String, transaction_item> selectedItems = new HashMap<>();

    public static RecyclerView pendingTransactionView;

    private static String barcode = null;

    private static String transaction_item_id;

    private static FragmentManager fm;

    public static String getBarcode() {
        return barcode;
    }

    public static boolean startChecking = false;

    public static InventoryDB inventoryDB;

    public static Item_quantity_dialog item_quantity_dialog;

    public static void setBarcode(String barcode) {
        support_methods.barcode = barcode;
    }

    public static void scanBarcode(Activity activity, String item_name){

        IntentIntegrator intentIntegrator = new IntentIntegrator(activity);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setCameraId(0);
        intentIntegrator.setPrompt("SCAN "+item_name);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.initiateScan();

    }

    public static void showDatePicker(Context context, final TextView textView){

        DatePickerDialog datePicker;

        final Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR); // getting the current year
        int month = calendar.get(Calendar.MONTH); // getting the current month
        final int day = calendar.get(Calendar.DAY_OF_MONTH); // getting the current day

//                showing the date picker after the input field is selected

        datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth +"/"+(month+1)+" /" +year;
                textView.setText(date);
            }
        }, year, month,day);

        datePicker.show();
    }

    public static ArrayList<transaction_item> getInventoryData(Context context){
        final ArrayList<transaction_item> transaction_items = new ArrayList<>();

        inventoryDB = new InventoryDB(context);
        Cursor res = inventoryDB.getAllData("inventory_data", 0);


        while (res.moveToNext()){

            transaction_items.add(new transaction_item(
                    res.getInt(0),
                    R.drawable.cart,
                    " "+res.getString(1),
                    res.getString(2) == null ? "None": res.getString(2) ,
                    " K "+res.getString(4),
                    " "+res.getString(6),
                    false ,
                    false,
                    res.getString(5).equals("1"),
                    "0",
                    res.getInt(3)
                    )
            );


        }

        return transaction_items;

    }

    public static void setListView(final Context context, final RecyclerView recent_transaction, boolean isTransaction){
//    creating an array of transaction_itemss to display on the listview

//        recent_transaction = findViewById(R.id.stock_view); // initialising the listView
        InventoryData inventoryData = new InventoryData();  // creating an instance of Inventory data class where there is a method to process the list view data and display it.
        inventoryData.setData(recent_transaction,context, support_methods.getInventoryData(context)); // passing data to the setData method

//        selection listenner to every item displayed
        if (!isTransaction)
            return;

        recent_transaction.addOnItemTouchListener(
                new RecyclerItemClickListener(context, recent_transaction ,new RecyclerItemClickListener.OnItemClickListener() {

                    @Override public void onItemClick(View view, int position) {
                        // do whatever                        TODO LISTVIEW LISTENER STOCK
                        if (!transactionItems.get(position).isSoled()) {
                            if (!transactionItems.get(position).isSelected()) {
                                int total = transactionItems.get(position).getRemaining_quantity();
                                int dbId = transactionItems.get(position).getDbId();

                                item_quantity_dialog = Item_quantity_dialog.newInstance(position, total, dbId );
                                item_quantity_dialog.show(fm, "QUANTITY_DIALOG");

                                vibrate(context);
                            } else {
                                transactionItems.get(position).setState(false);
                                transactionItems.get(position).setSelected(false);
                                selectedItems.remove(position + "");

                                if (selectedItems.size() == 0)
                                    TransactionHelper.transactionBTN.setVisibility(View.GONE);
                            }

                            transaction_adapter.notifyItemChanged(position);

                        }




                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // this is a long click listenner do whatever
                    }
                })
        );


    }

    public static String resolveMeasurement(int dbId){
        Cursor res = inventoryDB.getAllUnit();

        while (res.moveToNext()) {
            if (res.getInt(0) == dbId){
                return res.getString(1);
            }
        }
        return "";
    }
    public static void vibrate(Context context){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    public static void updateList(ArrayList<transaction_item> newList){
        support_methods.transactionItems = new ArrayList<>();

        support_methods.transactionItems.addAll(newList);
        transaction_adapter.notifyDataSetChanged();
    }

    public static FragmentManager getFm(){
        return fm;
    }

    public static void setFm(FragmentManager fm){
        support_methods.fm = fm;
    }

    public static boolean validateInput(String inputText){
        return TextUtils.isEmpty(inputText);
    }

    public static void showToast(Context context, String message){Toast.makeText(context, message, Toast.LENGTH_LONG).show();}
}
