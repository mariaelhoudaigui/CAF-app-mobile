package com.firstapp.exam;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnListe, btnCrud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnListe = findViewById(R.id.btnListe);
        btnCrud = findViewById(R.id.btnCrud);

        // Aller vers l'interface ListView
        btnListe.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            startActivity(intent);
        });

        // Aller vers l'interface CRUD
        btnCrud.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CrudActivity.class);
            startActivity(intent);
        });
    }
}
