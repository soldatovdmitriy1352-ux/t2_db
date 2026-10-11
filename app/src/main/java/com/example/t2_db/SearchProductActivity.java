package com.example.t2_db;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private EditText etSearch;
    private ListView lvSearchResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> searchResults;
    private ArrayList<String> allBarcodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_product);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        etSearch = findViewById(R.id.et_search);
        lvSearchResults = findViewById(R.id.lv_search_results);
        searchResults = new ArrayList<>();
        allBarcodes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResults);
        lvSearchResults.setAdapter(adapter);

        loadAllProducts();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProducts(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadAllProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                allBarcodes.add(barcode);
                searchResults.add(productName + " | " + barcode + " | " + price + " | " + quantity);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void filterProducts(String query) {
        searchResults.clear();
        if (query.isEmpty()) {
            searchResults.addAll(allBarcodes.size() > 0 ? getFullList() : new ArrayList<>());
        } else {
            for (int i = 0; i < allBarcodes.size(); i++) {
                Cursor cursor = databaseHelper.getProductByBarcode(allBarcodes.get(i));
                if (cursor != null && cursor.moveToFirst()) {
                    String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                    String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                    double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                    int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                    String displayText = productName + " | " + barcode + " | " + price + " | " + quantity;
                    
                    if (productName.toLowerCase().contains(query.toLowerCase()) ||
                        barcode.toLowerCase().contains(query.toLowerCase())) {
                        searchResults.add(displayText);
                    }
                    cursor.close();
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private ArrayList<String> getFullList() {
        ArrayList<String> fullList = new ArrayList<>();
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                fullList.add(productName + " | " + barcode + " | " + price + " | " + quantity);
            }
            cursor.close();
        }
        return fullList;
    }
}
