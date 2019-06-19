package com.susu.inventory_management_susu.helpers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.transaction_adapter_class;
import com.susu.inventory_management_susu.transaction_item;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class InventoryData {

    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager transaction_manager;

    public void setData(RecyclerView recyclerView, Context context, ArrayList<transaction_item> transaction_items){ //processing list view data and displaying them

        this.recyclerView = recyclerView;
        this.recyclerView.setHasFixedSize(true);
        TransactionHelper.transaction_recycler = this.recyclerView;

        transaction_manager = new LinearLayoutManager(context);
        support_methods.transaction_adapter = new transaction_adapter_class(transaction_items);

        this.recyclerView.setLayoutManager(transaction_manager);
        this.recyclerView.setAdapter(support_methods.transaction_adapter);

    }




}
