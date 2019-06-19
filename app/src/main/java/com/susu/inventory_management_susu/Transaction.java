package com.susu.inventory_management_susu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.helpers.InventoryData;
import com.susu.inventory_management_susu.helpers.RecyclerItemClickListener;
import com.susu.inventory_management_susu.helpers.TransactionHelper;
import com.susu.inventory_management_susu.helpers.support_methods;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import static android.support.constraint.Constraints.TAG;
import static android.view.View.VISIBLE;


public class Transaction extends Fragment {

    private RecyclerView recent_transaction;
    private ImageView search_icon;
    private SearchView search_input;
    private TextView top_label;

    public static Transaction newInstance() {
        return new Transaction();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view;

        view = inflater.inflate(R.layout.fragment_transaction, container, false); //loading thefragment layout
        setListView(view); //initialising ui controlls (Buttons)
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void init(final View view){
        search_icon = view.findViewById(R.id.search_icon);
        search_input = view.findViewById(R.id.search_input);
        top_label = view.findViewById(R.id.transaction_label);
        search_item();
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(getActivity() , SearchActivity.class);
//                startActivity(intent);
//                search_icon.setVisibility(View.GONE);
                if (top_label.getVisibility() == VISIBLE) {
                    top_label.setVisibility(View.GONE);
                    search_input.setVisibility(View.VISIBLE);
                }else{
                    top_label.setVisibility(View.VISIBLE);
                    search_input.setVisibility(View.GONE);
                }

            }
        });
    }

    public void setListView(View view){
//creating an array of transaction_itemss to display on the listview

        recent_transaction = view.findViewById(R.id.recent_transaction);// initialising the listView
        support_methods.setListView(getContext(), recent_transaction, true);
    }

    public void startNewActivity(Intent intent, Object[] data){
        intent.putExtra("item_data", data );
        startActivity(intent);
    }

public void search_item(){

// perform set on query text listener event
    search_input.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            RecyclerView.Adapter adapter = new transaction_adapter_class(TransactionHelper.filter(support_methods.transactionItems, newText));

            TransactionHelper.transaction_recycler.removeAllViews();
            TransactionHelper.transaction_recycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            return true;
        }
    });
}


}



