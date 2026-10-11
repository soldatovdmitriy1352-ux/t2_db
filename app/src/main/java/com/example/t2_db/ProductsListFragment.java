package com.example.t2_db;

import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class ProductsListFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ListView lvProducts;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> productList;

    public ProductsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_products_list, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        lvProducts = view.findViewById(R.id.lv_products);
        productList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, productList);
        lvProducts.setAdapter(adapter);

        registerForContextMenu(lvProducts);
        loadProducts();

        return view;
    }

    private void loadProducts() {
        productList.clear();
        Cursor cursor = databaseHelper.getAllProducts();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String productName = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                productList.add(productName + " | " + barcode + " | " + price + " | " + quantity);
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(R.id.lv_products, 1, 0, R.string.ctx_delete_product);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String selectedItem = productList.get(info.position);
            String[] parts = selectedItem.split(" \\| ");
            if (parts.length >= 2) {
                String barcode = parts[1].trim();
                databaseHelper.deleteProduct(barcode);
                loadProducts();
                Toast.makeText(requireContext(), R.string.toast_product_deleted, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
