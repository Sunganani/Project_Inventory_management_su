package com.susu.inventory_management_susu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.helpers.Navigation;
import com.susu.inventory_management_susu.helpers.ScanBarcode;
import com.susu.inventory_management_susu.helpers.TransactionHelper;
import com.susu.inventory_management_susu.helpers.support_methods;


public class add_item extends Fragment {
    private TextView item_barcode;
    private TextInputLayout item_name;
    private TextInputLayout item_price;
    private TextView select_date;
    private TextInputLayout quantity;
    private Button saveInventory;
    InventoryDB db;
    private ImageView home_img;

    Navigation navigation;

    public static add_item newInstance(View view) {
        return new add_item();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view){
        home_img = view.findViewById(R.id.home_img);
        item_name = view.findViewById(R.id.input_item_name);
        item_price = view.findViewById(R.id.input_item_price);
        select_date = view.findViewById(R.id.selectDate);
        saveInventory = view.findViewById(R.id.saveInventory);
        item_barcode = view.findViewById(R.id.input_barcode);
        quantity = view.findViewById(R.id.input_quantity);
        db = new InventoryDB(getContext());
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item, container, false);
        init(view);
        setControls(view);//calling method to initialise controls(Buttons) on this ui

        ((MainActivity) getActivity() ).passVal(new ScanBarcode() {
            @Override
            public void scanResult(String barcode) {
                item_barcode.setText(barcode);
            }
        });
        return view;
    }

//    method to initialise all controls found on this fragment layout
    private void setControls(final View view) {

        home_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).home();
            }
        });
//    datePicker select listenner
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support_methods.showDatePicker(getContext(), select_date);
                select_date.setTextColor(Color.GRAY);
            }
        });

        saveInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = item_name.getEditText().getText().toString();
                String price = item_price.getEditText().getText().toString();
                String date = select_date.getText().toString();
                String barcode = support_methods.getBarcode();
                String qua = quantity.getEditText().getText().toString();

                if(validateInput(name)) {
                    item_name.setError("Required");
                    item_name.setErrorEnabled(true);
                    return;
                }else{
                    item_name.setErrorEnabled(false);
                }

                if (validateInput(price)) {
                    item_price.setError("Required");
                    item_price.setErrorEnabled(true);
                    return;
                } else
                    item_price.setErrorEnabled(false);

//                if (validateInput(selectedDate)){
//                    select_date.setTextColor(Color.RED);
//                    select_date.setError("Required");
////                    return;
//                }

                submit(name, price, barcode, qua,"0", date);
            }
        });

        item_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support_methods.scanBarcode(getActivity(), "barcode");
            }
        });




    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void submit(String name, String price, String barcode, String quanty, String isSoled, String date){

        InventoryDB inventoryDB = new InventoryDB(getContext());
        boolean isInserted = inventoryDB.insertRecord(name,price,barcode, quanty ,isSoled,date);

        StringBuilder builder = new StringBuilder();
        builder.append("\n").append("Item Name: ").append(item_name.getEditText().getText()).append("\n");
        builder.append("Item Price: K").append(item_price.getEditText().getText()).append("\n");
        builder.append("Quantity: ").append(quantity.getEditText().getText()).append("\n");
        builder.append("Barcode: ").append(item_barcode.getText()).append("\n\n");

        if (isInserted){
            builder.append("ITEM SAVED!").append("\n");
            item_name.getEditText().setText("");
            item_price.getEditText().setText("");
            select_date.setText("");
            item_barcode.setText("");
            item_price.getEditText().setText("");
            quantity.getEditText().setText("");
            TransactionHelper.showAlert(getContext(), builder.toString());
        }else {
            builder.append("Oops something went Wrong!").append("\n");
            TransactionHelper.showAlert(getContext(), builder.toString());
        }
    }

    public boolean validateInput(String inputText){
        return TextUtils.isEmpty(inputText);
    }

}
