package com.susu.inventory_management_susu;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.susu.inventory_management_susu.helpers.support_methods;

import java.util.ArrayList;

public class transaction_adapter_class extends RecyclerView.Adapter<transaction_adapter_class.transaction_view_holder> {


//an adapter class for the list view (both list views)
    public static class transaction_view_holder extends RecyclerView.ViewHolder{

//        private ImageView image;
        private TextView name;
        private TextView barcode;
        private TextView price;
        private TextView date;
        private ImageView state;
        private TextView quantity;
        private TextView remaining_quantity;


        public transaction_view_holder(@NonNull View itemView) {
            super(itemView);

//            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.item_name);
            barcode = itemView.findViewById(R.id.item_barcode);
            price = itemView.findViewById(R.id.item_price);
            date = itemView.findViewById(R.id.item_date);
            state = itemView.findViewById(R.id.item_state);
            quantity = itemView.findViewById(R.id.quantity);
            remaining_quantity = itemView.findViewById(R.id.remaining_quantity);
        }
    }

    public transaction_adapter_class(ArrayList<transaction_item> transactionItems){
        support_methods.transactionItems = transactionItems;
    }

    @NonNull
    @Override
    public transaction_view_holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transaction_item, viewGroup, false);
        transaction_view_holder tvh = new transaction_view_holder(v);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final transaction_view_holder transaction_view_holder, int i) {
        transaction_item current_item = support_methods.transactionItems.get(i);

//        if (current_item.getState()){
//            transaction_view_holder.state1.setVisibility(View.VISIBLE);
//            transaction_view_holder.state2.setVisibility(View.GONE);
//        }else{
//            transaction_view_holder.state1.setVisibility(View.GONE);
//            transaction_view_holder.state2.setVisibility(View.VISIBLE);
//        }

//        transaction_view_holder.image.setImageResource(current_item.getImage());
        transaction_view_holder.name.setText(current_item.getName());
        transaction_view_holder.barcode.setText(current_item.getBarcode());
        transaction_view_holder.price.setText(current_item.getPrice());
        transaction_view_holder.date.setText(current_item.getDate());
        transaction_view_holder.quantity.setText(current_item.getQuantity());

        String rc = " remaining quantity: "+current_item.getRemaining_quantity()+"";
        transaction_view_holder.remaining_quantity.setText(rc);

        if (current_item.getState())
            transaction_view_holder.state.setVisibility(View.VISIBLE);
        else
            transaction_view_holder.state.setVisibility(View.GONE);
    }


    @Override
    public int getItemCount() {
        return support_methods.transactionItems.size();
    }

}
