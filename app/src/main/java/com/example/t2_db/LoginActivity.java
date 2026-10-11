package com.example.t2_db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText etFullName;
    private EditText etEmployeeCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etFullName = findViewById(R.id.et_full_name);
        etEmployeeCode = findViewById(R.id.et_employee_code);
    }

    public void OnNext(View view) {
        String fullName = etFullName.getText().toString().trim();
        String employeeCode = etEmployeeCode.getText().toString().trim();

        if (fullName.isEmpty() || employeeCode.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAdmin = AdminHelper.isAdmin(fullName, employeeCode);
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.KEY_IS_ADMIN, isAdmin);
        startActivity(intent);
        finish();
    }

    public void OnBack(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}