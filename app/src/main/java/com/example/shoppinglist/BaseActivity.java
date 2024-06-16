package com.example.shoppinglist;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    public ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setupDrawerAndToolbar(int toolbarId, int drawerLayoutId, int navViewId) {
        Toolbar toolbar = findViewById(toolbarId);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(drawerLayoutId);
        NavigationView navigationView = findViewById(navViewId);
        // aktualizacja ikony hamburgera w toolbarze
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // obsługa kliknięcia na elementy menu
        // utworzenie intencji oraz przejście do wybranego widoku
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent intent = new Intent(BaseActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_add_items) {
                Intent intent = new Intent(BaseActivity.this, AddItemActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_list_items) {
                Intent intent = new Intent(BaseActivity.this, ListItemsActivity.class);
                startActivity(intent);
            }
            drawerLayout.closeDrawers();
            return true;
        });
    }
    // metoda do obsłużenia zmiany pozycji ekranu
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
    // jeśli użytkownik kliknie na przycisk powrotu, zamknij szufladę
    // w innym przypadku powrót do poprzedniego widoku
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
