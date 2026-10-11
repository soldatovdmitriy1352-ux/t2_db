package com.example.t2_db;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.google.zxing.integration.android.IntentIntegrator;

public class AddProductFragment extends Fragment {
    private EditText etBarcode;
    private EditText etProductName;
    private EditText etPrice;
    private EditText etQuantity;
    private DatabaseHelper databaseHelper;

    // ActivityResultLauncher для сканирования штрихкода
    private final ActivityResultLauncher<Intent> scanLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_CANCELED) {
                    Toast.makeText(requireContext(), R.string.error_scan_cancelled, Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = result.getData();
                    if (intent != null) {
                        // ZXing возвращает результат в extra с ключом "SCAN_RESULT"
                        String contents = intent.getStringExtra("SCAN_RESULT");
                        if (contents != null) {
                            etBarcode.setText(contents);
                            Toast.makeText(requireContext(), R.string.toast_barcode_scanned, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add_product, container, false);

        etBarcode = view.findViewById(R.id.et_barcode);
        etProductName = view.findViewById(R.id.et_product_name);
        etPrice = view.findViewById(R.id.et_price);
        etQuantity = view.findViewById(R.id.et_quantity);
        databaseHelper = new DatabaseHelper(requireContext());

        Button btnScan = view.findViewById(R.id.btn_scan);
        btnScan.setOnClickListener(v -> scanBarcode());

        Button btnSave = view.findViewById(R.id.btn_save);
        btnSave.setOnClickListener(v -> onSave());

        return view;
    }

    private void scanBarcode() {
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Наведите камеру на штрихкод");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(true);
        integrator.setBarcodeImageEnabled(false);
        
        // Используем custom request code
        Intent intent = integrator.createScanIntent();
        scanLauncher.launch(intent);
    }

    private void onSave() {
        String barcode = etBarcode.getText().toString().trim();
        String productName = etProductName.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String quantityText = etQuantity.getText().toString().trim();

        if (barcode.isEmpty() || productName.isEmpty()) {
            Toast.makeText(requireContext(), R.string.error_empty_fields, Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        int quantity = 0;

        if (!priceText.isEmpty()) {
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), R.string.error_invalid_price, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (!quantityText.isEmpty()) {
            try {
                quantity = Integer.parseInt(quantityText);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), R.string.error_invalid_quantity, Toast.LENGTH_SHORT).show();
                return;
            }
        }

        long result = databaseHelper.addProduct(productName, barcode, price, quantity);
        if (result != -1) {
            Toast.makeText(requireContext(), R.string.toast_product_added, Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                getActivity().finish();
            }
        } else {
            Toast.makeText(requireContext(), R.string.error_duplicate_barcode, Toast.LENGTH_SHORT).show();
        }
    }
}
