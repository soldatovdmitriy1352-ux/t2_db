package com.example.t2_db;

public class AdminHelper {
    public static final String ADMIN_FULL_NAME = "Солдатов";
    public static final String ADMIN_EMPLOYEE_CODE = "341";

    public static boolean isAdmin(String fullName, String employeeCode) {
        return ADMIN_FULL_NAME.equals(fullName) && ADMIN_EMPLOYEE_CODE.equals(employeeCode);
    }
}
