package com.susu.inventory_management_susu;

import android.widget.CheckBox;
import android.widget.TextView;

import org.w3c.dom.Text;

public class transaction_item {
    private int dbId;
    private int image;
    private String name;
    private String barcode;
    private String price;
    private String date;
    private boolean state;
    private boolean isSelected;
    private boolean isSoled;
    private String quantity;
    private int remaining_quantity;

    public transaction_item(int dbId,int image, String name, String barcode,
                            String price, String date, boolean state, boolean isSelected,
                            boolean isSoled, String quantity, int remaining_quantity) {
        this.dbId = dbId;
        this.image = image;
        this.name = name;
        this.barcode = barcode;
        this.price = price;
        this.date = date;
        this.state = state;
        this.isSelected = isSelected;
        this.isSoled = isSoled;
        this.quantity = quantity;
        this.remaining_quantity = remaining_quantity;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getPrice() {
        return price;
    }

    public String getDate() {
        return date;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getDbId() {
        return dbId;
    }

    public int getRemaining_quantity() {
        return remaining_quantity;
    }

    public void setRemaining_quantity(int remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }

    public void setDbId(int dbId) {
        this.dbId = dbId;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSoled() {
        return isSoled;
    }

    public void setSoled(boolean soled) {
        isSoled = soled;
    }

    public boolean getState() {
        return state;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}

//this is the transaction item displayed on the list view used by the adapter class