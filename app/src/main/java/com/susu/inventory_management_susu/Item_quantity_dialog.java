package com.susu.inventory_management_susu;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.helpers.TransactionHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Item_quantity_dialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link Item_quantity_dialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Item_quantity_dialog extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "param1";
    private static final String TOTAL_QUANTITY = "param2";
    private static final String DB_ID = "param3";

    private EditText quantity_input;
    private Button ok;
    private Button cancel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private int position;
    private int total;
    private int db_id;

    InventoryDB db;



    private OnFragmentInteractionListener mListener;

    public Item_quantity_dialog() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Item_quantity_dialog newInstance(int position, int total, int db_id)  {
        Item_quantity_dialog fragment = new Item_quantity_dialog();
        Bundle args = new Bundle();
        args.putString(POSITION, position+"");
        args.putInt(TOTAL_QUANTITY, total );
        args.putInt(DB_ID, db_id );
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(POSITION);
            position = Integer.parseInt(mParam1);
            total = getArguments().getInt(TOTAL_QUANTITY);
            db_id = getArguments().getInt(DB_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item_quantity_dialog, container, false);
        init(view);

        return view;
    }

    public void init(View view){
        db = new InventoryDB(getContext());
        quantity_input = view.findViewById(R.id.input_quantity);
        ok = view.findViewById(R.id.accept_action);
        cancel = view.findViewById(R.id.cancel_action);

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        boolean state = false;

        switch (v.getId()){
            case R.id.accept_action:
                state = true;
             break;
            case R.id.cancel_action:
                state = false;
                break;
        }
        int remaining = total;

        if (!quantity_input.getText().toString().equals("")) {
            int q = Integer.parseInt(quantity_input.getText().toString());
            if (q > total){
                TransactionHelper.showAlert(getContext(), "Invalid transaction");
                return;
            }

            remaining = total - Integer.parseInt(quantity_input.getText().toString());

//            db.update(db_id, remaining);
        }

        mListener.setQuantity(position,quantity_input.getText().toString(), remaining, state);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setQuantity(int position, String quantity, int remaining, boolean action);
    }

//    private void updateInitSpinners(){
//        // you need to have a list of data that you want the spinner to display
//        List<String> spinnerArray =  new ArrayList<String>();
//
//        Cursor res = db.getAllUnit();
//
//        while (res.moveToNext()) {
//            spinnerArray.add(res.getString(1));
//        }
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, spinnerArray);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        units.setAdapter(adapter);
//    }
}
