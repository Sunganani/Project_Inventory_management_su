package com.susu.inventory_management_susu.helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.susu.inventory_management_susu.PendingTransactionAdapter;
import com.susu.inventory_management_susu.pending_transaction_item;
import com.susu.inventory_management_susu.transaction_item;

import java.util.ArrayList;
import java.util.List;

public class TransactionHelper {
    RecyclerView.LayoutManager transactionManager;
    public static Button transactionBTN;
    public static RecyclerView transaction_recycler;

    public void setRecyclerView(Context context, RecyclerView recyclerView, List<pending_transaction_item> listItems) {
        transactionManager = new LinearLayoutManager(context);
        support_methods.pending_transaction_adapter = new PendingTransactionAdapter(listItems, context);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(transactionManager);
        recyclerView.setAdapter(support_methods.pending_transaction_adapter);
    }

    public static void showAlert(Context context, String Message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(Message);
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

//        alertDialogBuilder.setNegativeButton("cancel",
//                new DialogInterface.OnClickListener() {
//
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//
//                    }
//                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static ArrayList<transaction_item> filter(List<transaction_item> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();

        final ArrayList<transaction_item> filteredModelList = new ArrayList<>();

        for (transaction_item model : models) {
            final String text = model.getName().toLowerCase();
            if (text.contains(lowerCaseQuery)) {
                filteredModelList.add(model);
            }
        }

        return filteredModelList;
    }
}
