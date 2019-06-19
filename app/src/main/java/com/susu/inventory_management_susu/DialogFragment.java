package com.susu.inventory_management_susu;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.helpers.RecyclerItemClickListener;
import com.susu.inventory_management_susu.helpers.TransactionHelper;
import com.susu.inventory_management_susu.helpers.support_methods;

import java.util.HashMap;
import java.util.Map;

import static com.susu.inventory_management_susu.helpers.support_methods.pending_transaction_items;
import static com.susu.inventory_management_susu.helpers.support_methods.selectedItems;

public class DialogFragment extends android.support.v4.app.DialogFragment {

    private TextView item_name;
    private TextView item_price;
    private TextView totalPriceView;
    private TextView transact;
    private RecyclerView recyclerView;
    String totalPrice = "K 0.00";
    TransactionHelper transactionHelper = new TransactionHelper();

    private DialogFragment.OnFragmentInteractionListener mListener;


    public DialogFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DialogFragment newInstance(String param1, String param2) {
        DialogFragment fragment = new DialogFragment();

        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogFragment.OnFragmentInteractionListener) {
            mListener = (DialogFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();

        getDialog().setTitle("Select item to Scan");
        getDialog().requestWindowFeature(Window.FEATURE_LEFT_ICON);
        getDialog().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.logo);

        totalPrice = "MK "+bundle.getString("total")+".00";

        View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        init(view);


        return view;
    }

    public void init(View view){
        totalPriceView = view.findViewById(R.id.barcode_display);
        transact = view.findViewById(R.id.save_item);
        recyclerView = view.findViewById(R.id.pending_transaction_items);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                pending_transaction_item item = pending_transaction_items.get(position);
                support_methods.transactionListItem.put(position+"", item);
                support_methods.scanBarcode(getActivity(), item.getItem_name());
                support_methods.pendingTransactionView = recyclerView;
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        transactionHelper.setRecyclerView(getContext(), recyclerView, pending_transaction_items );
        setAction();
    }

    public void setAction(){

        totalPriceView.setText(totalPrice);

        transact.setOnClickListener(new View.OnClickListener() {
            InventoryDB db = new InventoryDB(getContext());

            @Override
            public void onClick(View v) {
                Map<String, String> itemValues = new HashMap<>();

                for (pending_transaction_item item: pending_transaction_items){

                    itemValues.put("item_name", item.getItem_name());
                    itemValues.put("item_quantity", item.getItem_quantity());
                    itemValues.put("item_price", item.getItem_price());
                    itemValues.put("date_sold", item.getDate_sold());
//                    insertTransaction();
                    db.update(item.getDbId(), item.getRemaining_quantity());
                    mListener.selectedMenu("close");
                }

                db.insertTransaction(itemValues);

                transactionHelper.setRecyclerView(getContext(), recyclerView, pending_transaction_items );
                selectedItems.clear();

                Toast.makeText(getContext(), "Complete", Toast.LENGTH_LONG).show();
            }

        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void selectedMenu(String act);
    }

}
