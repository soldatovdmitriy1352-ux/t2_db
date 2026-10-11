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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class AddProductActivity extends AppCompatActivity {
    private EditText etBarcode;
    private EditText etProductName;
    private EditText etPrice;
    private EditText etQuantity;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etBarcode = findViewById(R.id.et_barcode);
        etProductName = findViewById(R.id.et_product_name);
        etPrice = findViewById(R.id.et_price);
        etQuantity = findViewById(R.id.et_quantity);
        databaseHelper = new DatabaseHelper(this);
    }

    public void OnScanBarcode(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Наведите камеру на штрихкод");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, R.string.error_scan_cancelled, Toast.LENGTH_SHORT).show();
            } else {
                etBarcode.setText(result.getContents());
                Toast.makeText(this, R.string.toast_barcode_scanned, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void OnSave(View view) {
        String barcode = etBarcode.getText().toString().trim();
        String productName = etProductName.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();

        if (barcode.isEmpty() || productName.isEmpty()) {
            Toast.makeText(this, R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        int quantity = 0;

        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.error_invalid_price, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!quantityText.isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                Toast.makeText(this, R.string.error_invalid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        long result = databaseHelper.addProduct(productName, barcode, price, quantity);
        if (result != -1) {
            Toast.makeText(this, R.string.toast_product_added, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, R.string.error_duplicate_barcode, Toast.LENGTH_SHORT).show();
        }
    }
}
