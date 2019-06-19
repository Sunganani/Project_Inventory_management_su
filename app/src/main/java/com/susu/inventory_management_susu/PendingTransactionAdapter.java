package com.susu.inventory_management_susu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.susu.inventory_management_susu.helpers.support_methods;

import java.util.List;

public class PendingTransactionAdapter extends RecyclerView.Adapter<PendingTransactionAdapter.ViewHolder> {

    Context context;

    public PendingTransactionAdapter(List<pending_transaction_item> pending_transaction_items, Context context) {
        support_methods.pendingTransactionItemList = pending_transaction_items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pending_transaction_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        pending_transaction_item item = support_methods.pendingTransactionItemList.get(i);

        viewHolder.item_name.setText(item.getItem_name());
        viewHolder.item_price.setText(item.getItem_price());
        viewHolder.item_barcode.setText(item.getItem_barcode());
        viewHolder.scanned_barcode.setText(item.getScannedBarcode());
    }

    @Override
    public int getItemCount() {
        return support_methods.pendingTransactionItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_name, item_price, item_barcode, scanned_barcode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_name = itemView.findViewById(R.id.item_name);
            item_price = itemView.findViewById(R.id.item_price);
            item_barcode = itemView.findViewById(R.id.item_barcode);
            scanned_barcode = itemView.findViewById(R.id.item_barcode2);
        }
    }
}
