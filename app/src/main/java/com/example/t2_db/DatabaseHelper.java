package com.example.t2_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "t2_db.db";
    private static final int DB_VERSION = 2;
    private static final String TABLE_USERS = "Users";
    private static final String TABLE_PRODUCTS = "Products";

    private static final String COL_ID = "_id";
    private static final String COL_FULL_NAME = "fullName";
    private static final String COL_EMPLOYEE_CODE = "employeeCode";
    private static final String COL_PRODUCT_NAME = "productName";
    private static final String COL_BARCODE = "barcode";
    private static final String COL_PRICE = "price";
    private static final String COL_QUANTITY = "quantity";

    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_FULL_NAME + " TEXT NOT NULL, " +
                    COL_EMPLOYEE_CODE + " TEXT NOT NULL UNIQUE" +
                    ")";

    private static final String CREATE_TABLE_PRODUCTS =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PRODUCT_NAME + " TEXT NOT NULL, " +
                    COL_BARCODE + " TEXT NOT NULL UNIQUE, " +
                    COL_PRICE + " REAL NOT NULL DEFAULT 0, " +
                    COL_QUANTITY + " INTEGER NOT NULL DEFAULT 0" +
                    ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_PRODUCTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(db);
    }

    public long addUser(String fullName, String employeeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_FULL_NAME, fullName);
        values.put(COL_EMPLOYEE_CODE, employeeCode);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }

    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public Cursor getUserByCode(String employeeCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS +
                " WHERE " + COL_EMPLOYEE_CODE + " = ?", new String[]{employeeCode});
    }

    public void deleteUser(String employeeCode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, COL_EMPLOYEE_CODE + " = ?", new String[]{employeeCode});
        db.close();
    }

    // Product methods
    public long addProduct(String productName, String barcode, double price, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PRODUCT_NAME, productName);
        values.put(COL_BARCODE, barcode);
        values.put(COL_PRICE, price);
        values.put(COL_QUANTITY, quantity);
        long result = db.insert(TABLE_PRODUCTS, null, values);
        db.close();
        return result;
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS, null);
    }

    public Cursor getProductByBarcode(String barcode) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTS +
                " WHERE " + COL_BARCODE + " = ?", new String[]{barcode});
    }

    public void deleteProduct(String barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PRODUCTS, COL_BARCODE + " = ?", new String[]{barcode});
        db.close();
    }
}
