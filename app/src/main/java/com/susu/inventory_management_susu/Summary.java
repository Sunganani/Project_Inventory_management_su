package com.susu.inventory_management_susu;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.susu.inventory_management_susu.db.InventoryDB;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Summary.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Summary#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Summary extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int total;
    private int sold;

    private TextView total_label;
    private TextView sold_label;
    private TextView remaining_label;
    private InventoryDB inventoryDB;
    private ImageView home_img;

    int total_in_stock = 0;



    private OnFragmentInteractionListener mListener;

    public Summary() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Summary newInstance() {
        Summary fragment = new Summary();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_summary, container, false);
        init(view);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    private void init(View view){
        inventoryDB = new InventoryDB(getContext());
        sold_label = view.findViewById(R.id.sold_label);
        remaining_label = view.findViewById(R.id.remaining_label);
        home_img = view.findViewById(R.id.home_img);

        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).home();
            }
        });
        setEstimatedEarning();
        incomeIn();
//        setSold();
    }

    private void incomeIn() {
        int total = 0;

        Cursor res = inventoryDB.getAllData("transaction_table");

        while(res.moveToNext()){

            String sp = res.getString(4);
            sp = sp.replace("K", "");
            sp = sp.replace(" ", "");

            total += (res.getInt(3) * Integer.parseInt(sp));
        }

        remaining_label.setText(formatMoney(total));
    }

    private void setEstimatedEarning() {
        int total = 0;

        Cursor res = inventoryDB.getAllData("inventory_data");

        while(res.moveToNext()){
            total += (res.getInt(4) * res.getInt(3));
        }

        total_in_stock = total;
        sold_label.setText(formatMoney(total));
    }

    public String formatMoney(int amount){
        Locale locale = new Locale("en", "MW");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        return currencyFormatter.format(amount);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        void onFragmentInteraction(Uri uri);
    }
}
