package com.example.t2_db;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_IS_ADMIN = "isAdmin";
    private boolean isAdmin = false;

    public static void setAdminMode(MainActivity activity, boolean isAdmin) {
        activity.isAdmin = isAdmin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (getIntent() != null) {
            isAdmin = getIntent().getBooleanExtra(KEY_IS_ADMIN, false);
        }

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        
        if (isAdmin) {
            setupAdminNavigation(bottomNavigation);
        } else {
            setupUserNavigation(bottomNavigation);
        }

        if (savedInstanceState == null) {
            if (isAdmin) {
                loadFragment(new AdminFragment());
                bottomNavigation.setSelectedItemId(R.id.nav_admin_users);
            } else {
                loadFragment(new AddProductFragment());
                bottomNavigation.setSelectedItemId(R.id.nav_add_product);
            }
        }
    }

    private void setupUserNavigation(BottomNavigationView bottomNavigation) {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_add_product) {
                loadFragment(new AddProductFragment());
                return true;
            } else if (itemId == R.id.nav_products_list) {
                loadFragment(new ProductsListFragment());
                return true;
            } else if (itemId == R.id.nav_search) {
                loadFragment(new SearchProductFragment());
                return true;
            } else if (itemId == R.id.nav_contacts) {
                loadFragment(new ContactFragment());
                return true;
            }
            return false;
        });
    }

    private void setupAdminNavigation(BottomNavigationView bottomNavigation) {
        bottomNavigation.getMenu().clear();
        bottomNavigation.inflateMenu(R.menu.navigation_menu_admin);
        
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_add_product) {
                loadFragment(new AddProductFragment());
                return true;
            } else if (itemId == R.id.nav_products_list) {
                loadFragment(new ProductsListFragment());
                return true;
            } else if (itemId == R.id.nav_search) {
                loadFragment(new SearchProductFragment());
                return true;
            } else if (itemId == R.id.nav_admin_users) {
                loadFragment(new AdminFragment());
                return true;
            } else if (itemId == R.id.nav_contacts) {
                loadFragment(new ContactFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fl_fragment_container, fragment)
                .commit();
    }
}