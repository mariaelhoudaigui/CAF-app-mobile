package com.firstapp.exam;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    DatabaseHelper dbHelper;
    ArrayList<HashMap<String, String>> matchs; // ← HashMap pour SimpleAdapter

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
            HashMap<String, String> map = new HashMap<>();
            map.put("eq1", cursor.getString(0));
            map.put("eq2", cursor.getString(1));
            map.put("date", "📅 " + cursor.getString(2));
            map.put("stade", "🏟 " + cursor.getString(3));

            matchs.add(map);
        }

        cursor.close();

        String[] from = {"eq1", "eq2", "date", "stade"};
        int[] to = {R.id.tvEquipe1, R.id.tvEquipe2, R.id.tvDate, R.id.tvStade};

        SimpleAdapter simpleAdapter = new SimpleAdapter(
                this,
                matchs,
                R.layout.item_match, // ← ton layout ConstraintLayout
                from,
                to
        );

        listView.setAdapter(simpleAdapter);
    }
}
