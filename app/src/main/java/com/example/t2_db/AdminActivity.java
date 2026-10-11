package com.example.t2_db;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private ListView lvUsers;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);
        lvUsers = findViewById(R.id.lv_users);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);

        registerForContextMenu(lvUsers);
        loadUsers();
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

    public void OnAddUser(View view) {
        Intent intent = new Intent(this, AddUserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(R.id.lv_users, 1, 0, R.string.ctx_delete_user);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String selectedItem = userList.get(info.position);
            String employeeCode = selectedItem.substring(selectedItem.lastIndexOf('(') + 1, selectedItem.lastIndexOf(')'));
            databaseHelper.deleteUser(employeeCode);
            loadUsers();
            Toast.makeText(this, R.string.user_deleted, Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}
