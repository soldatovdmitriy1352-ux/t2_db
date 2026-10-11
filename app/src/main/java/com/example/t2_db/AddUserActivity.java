package com.example.t2_db;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddUserActivity extends AppCompatActivity {
    private EditText etFullName;
    private EditText etEmployeeCode;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFullName = findViewById(R.id.et_full_name);
        etEmployeeCode = findViewById(R.id.et_employee_code);
        databaseHelper = new DatabaseHelper(this);
    }

    public void OnSave(View view) {
        String fullName = etFullName.getText().toString().trim();
        String employeeCode = etEmployeeCode.getText().toString().trim();

        if (fullName.isEmpty() || employeeCode.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.addUser(fullName, employeeCode);
        if (result != -1) {
            Toast.makeText(this, R.string.user_added_success, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.error_duplicate_code, Toast.LENGTH_SHORT).show();
        }
    }
}
