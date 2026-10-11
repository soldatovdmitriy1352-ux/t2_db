package com.example.t2_db;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class AdminFragment extends Fragment {
    private DatabaseHelper databaseHelper;
    private ListView lvUsers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;

    public AdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_admin, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        lvUsers = view.findViewById(R.id.lv_users);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);

        registerForContextMenu(lvUsers);
        loadUsers();

        Button btnAddUser = view.findViewById(R.id.btn_add_user);
        btnAddUser.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddUserActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUsers() {
        userList.clear();
        Cursor cursor = databaseHelper.getAllUsers();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String fullName = cursor.getString(cursor.getColumnIndexOrThrow("fullName"));
                String employeeCode = cursor.getString(cursor.getColumnIndexOrThrow("employeeCode"));
                userList.add(fullName + " (" + employeeCode + ")");
            }
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(R.id.lv_users, 1, 0, R.string.ctx_delete_user);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String selectedItem = userList.get(info.position);
            String employeeCode = selectedItem.substring(selectedItem.lastIndexOf('(') + 1, selectedItem.lastIndexOf(')'));
            databaseHelper.deleteUser(employeeCode);
            loadUsers();
            Toast.makeText(requireContext(), R.string.user_deleted, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
