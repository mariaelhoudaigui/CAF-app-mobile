package com.firstapp.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper dbHelper;
    ArrayList<String> matchs;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        listView = findViewById(R.id.listViewEtudiants);
        dbHelper = new DatabaseHelper(this);

        matchs = new ArrayList<>();

        afficherMatchs();
    }

    private void afficherMatchs() {
        matchs.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT equipe1, equipe2, date_match, stade FROM match_afrique",
                null
        );

        while (cursor.moveToNext()) {
            String ligne =
                    cursor.getString(0) + " vs " + cursor.getString(1) + "\n" +
                            "📅 " + cursor.getString(2) + " | 🏟 " + cursor.getString(3);

            matchs.add(ligne);
        }

        cursor.close();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                matchs
        );

        listView.setAdapter(adapter);
    }
}
