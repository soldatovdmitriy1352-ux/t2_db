package com.example.t2_db;

import android.database.Cursor;

public class Product {
    private int id;
    private String productName;
    private String barcode;
    private double price;
    private int quantity;

    public Product() {
    }

    public Product(int id, String productName, String barcode, double price, int quantity) {
        this.id = id;
        this.productName = productName;
        this.barcode = barcode;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public static Product fromCursor(Cursor cursor) {
        Product product = new Product();
        product.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        product.setProductName(cursor.getString(cursor.getColumnIndexOrThrow("productName")));
        product.setBarcode(cursor.getString(cursor.getColumnIndexOrThrow("barcode")));
        product.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
        product.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
        return product;
    }
}
