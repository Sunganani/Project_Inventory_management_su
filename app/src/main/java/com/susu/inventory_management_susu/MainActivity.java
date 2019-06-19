package com.susu.inventory_management_susu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.susu.inventory_management_susu.db.InventoryDB;
import com.susu.inventory_management_susu.helpers.Navigation;
import com.susu.inventory_management_susu.helpers.ScanBarcode;
import com.susu.inventory_management_susu.helpers.TransactionHelper;
import com.susu.inventory_management_susu.helpers.support_methods;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.IntStream;

import static com.susu.inventory_management_susu.helpers.support_methods.pendingTransactionView;
import static com.susu.inventory_management_susu.helpers.support_methods.pending_transaction_items;
import static com.susu.inventory_management_susu.helpers.support_methods.selectedItems;
import static com.susu.inventory_management_susu.helpers.support_methods.transactionItems;
import static com.susu.inventory_management_susu.helpers.support_methods.transactionListItem;

public class MainActivity extends AppCompatActivity implements DialogFragment.OnFragmentInteractionListener,
        Item_quantity_dialog.OnFragmentInteractionListener, Summary.OnFragmentInteractionListener {

    private Button scan_btn;
    private Fragment currentFragment;
    private Boolean show_scan_btn = true;
    private FragmentManager fm = getSupportFragmentManager();
    private add_item addItem = new add_item();
    DialogFragment dialogFragment;
    ScanBarcode scanBarcode;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_transaction:
                    currentFragment = Transaction.newInstance();
                    switchFragments(currentFragment);
                    scan_btn.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_show_item:
                    currentFragment = Summary.newInstance();
                    switchFragments(currentFragment);
                    scan_btn.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_add_item:
                    currentFragment = add_item.newInstance(scan_btn);
                    switchFragments(currentFragment);
                    scan_btn.setVisibility(View.GONE);
                    return true;
            }
            return false;
        }
    };

    public void setScan_btn(){
        scan_btn.setVisibility(View.GONE);

    }

//first method to be called this is where we are loading the main view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InventoryDB inventoryDB = new InventoryDB(getApplicationContext());
        if (savedInstanceState != null){
            return;
        }

        currentFragment = Transaction.newInstance();//creating an instace of Transaction fragment
        switchFragments(currentFragment); // calling method to load the fragment container.

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation); // initialising bottom navigation
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener); // adding selection listener to the bottom navigation

        scan_btn = findViewById(R.id.transact); //Iitialising btn button
        TransactionHelper.transactionBTN = scan_btn;
        final Activity activity = this;

         int overall = 0;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//          barcode Scanner
//            support_methods.scanBarcode(activity);
                if (pending_transaction_items.size() > 0){ pending_transaction_items.clear();}

                ArrayList<Integer> total = new ArrayList<>();

                for (Map.Entry<String, transaction_item> entry : selectedItems.entrySet()) {

                    pending_transaction_items.add(new pending_transaction_item(
                            entry.getValue().getDbId(),
                            entry.getValue().getName(),
                            entry.getValue().getPrice(),
                            entry.getValue().getBarcode(),
                            "",
                            entry.getValue().toString().equals(""),
                            entry.getValue().getQuantity(),
                            entry.getValue().getDate(),
                            entry.getValue().getRemaining_quantity()
                            )

                    );
                    String value = entry.getValue().getPrice();
                    value = value.replace("K","");
                    value = value.replace(" ","");
                    try{
                        int num = Integer.parseInt(value);
                        total.add(num);

                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "invalid price", Toast.LENGTH_LONG).show();
                    }
                }

                int sum = 0;
                for (Integer num: total ){

                    sum += num;
                }


                dialogFragment = new DialogFragment();
                Bundle bundle = new Bundle();

                bundle.putString("total", sum+"" );

                dialogFragment.setArguments(bundle);
                dialogFragment.show(fm, "transaction");

            }
        });

        support_methods.setFm(getSupportFragmentManager());
    }

//    processing results produced by a barcode scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult Result = IntentIntegrator.parseActivityResult(requestCode , resultCode ,data);

        if(Result != null){
            if(Result.getContents() == null){
                Toast.makeText(this, "cancelled", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Scanned -> " + Result.getContents(), Toast.LENGTH_SHORT).show();
                support_methods.setBarcode(Result.getContents());

                if (scanBarcode != null)
                    scanBarcode.scanResult(Result.getContents());

                for (Map.Entry<String, pending_transaction_item> entry : support_methods.transactionListItem.entrySet()) {

                    support_methods.transactionListItem.get(entry.getKey()).setScannedBarcode(Result.getContents());
                    support_methods.pending_transaction_adapter.notifyItemChanged(Integer.parseInt(entry.getKey()));

                }
//                dialogFragment.showBarcode();
                Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
            }
        }
        else {
            super.onActivityResult(requestCode , resultCode , data);
        }
    }


    private void startNewActivity(Intent intent){
        startActivity(intent);
        overridePendingTransition(R.anim.slide_up,  R.anim.no_animation);
    }

//    method to swit fragments
public void switchFragments(Fragment newFragment){
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction transaction = fragmentManager.beginTransaction();
    transaction.replace(R.id.fragment_container, newFragment);
    transaction.commit();

}

    public Button getScan_btn() {
        return scan_btn;
    }


    @Override
    public void selectedMenu(String act) {
        dialogFragment.dismiss();
        currentFragment = Transaction.newInstance();
        switchFragments(currentFragment);
    }

    public void passVal(ScanBarcode scanBarcode) {
        this.scanBarcode = scanBarcode;
    }

    public void home(){
        currentFragment = Transaction.newInstance();
        switchFragments(currentFragment);
        scan_btn.setVisibility(View.GONE);
    }

    @Override
    public void setQuantity(int position, String quantity, int remaining, boolean action_state) {
        if (action_state){
            transactionItems.get(position).setQuantity(quantity);
            transactionItems.get(position).setRemaining_quantity(remaining);
//            transactionItems.get(position).setR
            transactionItems.get(position).setState(true);
            transactionItems.get(position).setSelected(true);
            selectedItems.put(position + "", transactionItems.get(position));
        }

        if ( selectedItems.size() > 0 ){
            TransactionHelper.transactionBTN.setVisibility(View.VISIBLE);
        }else {
            TransactionHelper.transactionBTN.setVisibility(View.GONE);
        }

        support_methods.item_quantity_dialog.dismiss();
        support_methods.transaction_adapter.notifyItemChanged(position);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
