package com.example.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ListItemsActivity extends BaseActivity{

    private AppDatabase db;
    private List<Item> itemList;
    private MyAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        setupDrawerAndToolbar(R.id.toolbar, R.id.drawer_layout, R.id.nav_view);


        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        updateItemList();
        itemList = db.itemDAO().getAll();

        // Jeśli lista nie jest pusta wyświetl listę produktów
        if (itemList != null && !itemList.isEmpty()) {
            adapter = new MyAdapter(itemList);
            recyclerView.setAdapter(adapter);
            // Słuchacz do przycisku Usuń
            // Usuwa produkt z bazy danych oraz wyświetla komunikat
            adapter.setOnItemClickListener(position -> {
                Item item = itemList.get(position);
                db.itemDAO().delete(item);
                itemList.remove(position);
                adapter.notifyItemRemoved(position);
                Toast.makeText(ListItemsActivity.this, "Produkt został usunięty.", Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Nie dodałeś jeszcze żadnych produktów.", Toast.LENGTH_SHORT).show();
        }
        Button addItemBtn = findViewById(R.id.addNewItembtn);
        addItemBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ListItemsActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

    private void updateItemList() {
        itemList = db.itemDAO().getAll();
        if (itemList == null || itemList.isEmpty()) {
            Toast.makeText(this, "Nie dodałeś jeszcze żadnych produktów.", Toast.LENGTH_SHORT).show();
        }
    }
}
