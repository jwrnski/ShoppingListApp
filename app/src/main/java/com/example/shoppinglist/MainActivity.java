package com.example.shoppinglist;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ustawienie szuflady oraz paska narzędzi
        setupDrawerAndToolbar(R.id.toolbar, R.id.drawer_layout, R.id.nav_view);
        // konfiguracja bazy danych
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();
        // pobranie danych z bazy danych
        final List<Item>[] myDataset = new List[]{db.itemDAO().getAll()};

        // słuchacz do przycisku dodawania nowego produktu
        // Tworzy nową intecję do aktywności AddItemActivity
       Button button = findViewById(R.id.addItemButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // Uruchomienie serwisu do wysyłania powiadomień
        Intent serviceIntent = new Intent(this, AppService.class);
        startService(serviceIntent);
    }

    // Zatrzymanie serwisu przy wyłączeniu aplikacji
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent serviceIntent = new Intent(this, AppService.class);
        stopService(serviceIntent);
    }
}
