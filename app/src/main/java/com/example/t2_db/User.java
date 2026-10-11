package com.example.t2_db;

import android.database.Cursor;

public class User {
    private int id;
    private String fullName;
    private String employeeCode;

    public User() {
    }

    public User(int id, String fullName, String employeeCode) {
        this.id = id;
        this.fullName = fullName;
        this.employeeCode = employeeCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public static User fromCursor(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(cursor.getColumnIndexOrThrow("_id")));
        user.setFullName(cursor.getString(cursor.getColumnIndexOrThrow("fullName")));
        user.setEmployeeCode(cursor.getString(cursor.getColumnIndexOrThrow("employeeCode")));
        return user;
    }
}
