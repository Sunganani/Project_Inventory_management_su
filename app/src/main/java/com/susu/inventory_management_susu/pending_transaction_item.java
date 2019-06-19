package com.susu.inventory_management_susu;

public class pending_transaction_item {
    private int dbId;
    private String item_name;
    private String item_price;
    private String item_barcode;
    private boolean hasBarcode;
    private String scannedBarcode;
    private String item_quantity;
    private String date_sold;
    private int remaining_quantity;

    public pending_transaction_item(int dbId, String item_name, String item_price,
                                    String item_barcode, String scannedBarcode, boolean hasBarcode,
                                    String item_quantity, String date_sold, int remaining_quantity) {
        this.dbId = dbId;
        this.item_name = item_name;
        this.item_price = item_price;
        this.item_barcode = item_barcode;
        this.hasBarcode = hasBarcode;
        this.scannedBarcode = scannedBarcode;
        this.item_quantity = item_quantity;
        this.date_sold = date_sold;
        this.remaining_quantity = remaining_quantity;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getDate_sold() {
        return date_sold;
    }

    public int getRemaining_quantity() {
        return remaining_quantity;
    }

    public void setRemaining_quantity(int remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
    }

    public void setDate_sold(String date_sold) {
        this.date_sold = date_sold;
    }

    public String getItem_barcode() {
        return item_barcode;
    }

    public int getDbId() {
        return dbId;
    }

    public String getScannedBarcode() {
        return scannedBarcode;
    }

    public void setScannedBarcode(String scannedBarcode) {
        this.scannedBarcode = scannedBarcode;
    }

    public void setItem_barcode(String item_barcode) {
        this.item_barcode = item_barcode;
    }

    public boolean isHasBarcode() {
        return hasBarcode;
    }

    public void setHasBarcode(boolean hasBarcode) {
        this.hasBarcode = hasBarcode;
    }
}
