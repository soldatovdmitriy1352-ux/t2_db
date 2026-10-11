package com.example.t2_db;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class SearchProductFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private EditText etSearch;
    private ListView lvSearchResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> searchResults;
    private ArrayList<String> allProducts;

    public SearchProductFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_product, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        etSearch = view.findViewById(R.id.et_search);
        lvSearchResults = view.findViewById(R.id.lv_search_results);
        searchResults = new ArrayList<>();
        allProducts = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, searchResults);
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

        return view;
    }

    private void loadAllProducts() {
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                allProducts.add(productName + " | " + barcode + " | " + price + " | " + quantity);
            }
            cursor.close();
        }
        searchResults.addAll(allProducts);
        adapter.notifyDataSetChanged();
    }

    private void filterProducts(String query) {
        searchResults.clear();
        if (query.isEmpty()) {
            searchResults.addAll(allProducts);
        } else {
            for (String product : allProducts) {
                if (product.toLowerCase().contains(query.toLowerCase())) {
                    searchResults.add(product);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
