package com.example.shoppinglist;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends BaseActivity {
    private EditText amountEditText;
    private Spinner productSpinner;
    private ImageView productImageView;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        setupDrawerAndToolbar(R.id.toolbar, R.id.drawer_layout, R.id.nav_view);

        amountEditText = findViewById(R.id.amountEditText);
        productSpinner = findViewById(R.id.productSpinner);
        productImageView = findViewById(R.id.productImageView);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name").allowMainThreadQueries().build();

        // ustawienie obiektu klasy Spinner - menu typu dropdown z nazwami produktów
        setupSpinner();

        // ustawienie słuchacza dla przycisku dodania produktu i wyświetlenie komunikatu
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            // pobranie nazwy produktu z menu i ilości z edytowalnego pola tekstowego
            String selectedProductName = productSpinner.getSelectedItem().toString();
            int amount = Integer.parseInt(amountEditText.getText().toString());
            if (!selectedProductName.isEmpty()) {
                // utworzenie nowego obiektu Item
                Item item = new Item();
                // ustawienie wartości produktu
                item.setName(selectedProductName);
                item.setAmount(amount);
                // dodanie rozszerzenia do nazwy produktu - nazwa obrazka
                item.setImage(selectedProductName + ".png");
                // dodanie obiektu do bazy danych
                db.itemDAO().insertAll(item);
                Toast.makeText(AddItemActivity.this, "Produkt został dodany do listy.", Toast.LENGTH_SHORT).show();
                // zakończenie aktywności i przejście do poprzedniego widoku
                db.itemDAO().getAll();
                finish();
            }
        });
        // Słuchacz do wyboru produktu z menu oraz wyświetlenie odpowiedniego obrazka
        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedProductName = (String) parent.getItemAtPosition(position);
                displayProductImage(selectedProductName);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Dodanie nazw produktów do listy
    private List<String> getProductNames() {
        List<String> productNames = new ArrayList<>();
        productNames.add("apple");
        productNames.add("bread");
        productNames.add("milk");
        productNames.add("eggs");
        productNames.add("water");
        productNames.add("butter");
        productNames.add("wheat");
        return productNames;
    }

    // ustawienie spinnera - pobranie nazw produktów
    // oraz ustawienie adaptera dla spinnera
    private void setupSpinner() {
        List<String> productNames = getProductNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        productSpinner.setAdapter(adapter);
    }

    // wyświetlenie obrazka na podstawie nazwy produktu
    private void displayProductImage(String productName) {
        int resourceId = getResources().getIdentifier(productName.toLowerCase(), "drawable", getPackageName());
        if (resourceId != 0) {
            productImageView.setImageResource(resourceId);
            productImageView.setVisibility(View.VISIBLE);
        } else {
            productImageView.setVisibility(View.GONE);
        }
    }

}