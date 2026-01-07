package com.firstapp.exam;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.DatePickerDialog;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import android.widget.AutoCompleteTextView;

public class CrudActivity extends AppCompatActivity {

    AutoCompleteTextView editEquipe1, editEquipe2;
    EditText editDate, editStade;
    Button btnAjouter, btnModifier, btnSupprimer;
    ListView listView;

    DatabaseHelper dbHelper;

    ArrayList<Integer> ids;
    ArrayList<String> matchs;
    ArrayAdapter<String> adapter;

    int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crud);

        // Liaison XML
        editEquipe1 = findViewById(R.id.editEquipe1);
        editEquipe2 = findViewById(R.id.editEquipe2);
        editDate = findViewById(R.id.editDate);
        editStade = findViewById(R.id.editStade);

        btnAjouter = findViewById(R.id.btnAjouter);
        btnModifier = findViewById(R.id.btnModifier);
        btnSupprimer = findViewById(R.id.btnSupprimer);
        listView = findViewById(R.id.listViewMatchs);

        dbHelper = new DatabaseHelper(this);

        ids = new ArrayList<>();
        matchs = new ArrayList<>();

        afficherMatchs();
        String[] paysAfricains = {
                "Maroc", "Algérie", "Tunisie", "Égypte", "Sénégal",
                "Côte d’Ivoire", "Nigeria", "Ghana", "Cameroun",
                "Mali", "Burkina Faso", "Guinée", "Afrique du Sud",
                "RD Congo", "Zambie", "Angola", "Cap-Vert",
                "Gabon", "Bénin", "Ouganda", "Kenya", "Tanzanie"
        };
        ArrayAdapter<String> paysAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                paysAfricains
        );

        editEquipe1.setAdapter(paysAdapter);
        editEquipe2.setAdapter(paysAdapter);
        editEquipe1.setThreshold(1);
        editEquipe2.setThreshold(1);
        editEquipe1.setOnClickListener(v -> editEquipe1.showDropDown());
        editEquipe2.setOnClickListener(v -> editEquipe2.showDropDown());


        editDate.setOnClickListener(v -> {

            // Date actuelle
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Création du DatePicker
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    CrudActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {

                        // Format jj/mm/aaaa
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editDate.setText(date);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });

        btnAjouter.setOnClickListener(v -> {
            ajouterMatch();
            afficherMatchs();
        });

        btnModifier.setOnClickListener(v -> {
            modifierMatch();
            afficherMatchs();
        });

        btnSupprimer.setOnClickListener(v -> {
            supprimerMatch();
            afficherMatchs();
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedId = ids.get(position);

                SQLiteDatabase db = dbHelper.getReadableDatabase();

                Cursor cursor = db.query(
                        DatabaseHelper.TABLE_MATCH,
                        null,
                        DatabaseHelper.COL_ID + "=?",
                        new String[]{String.valueOf(selectedId)},
                        null,
                        null,
                        null
                );

                if (cursor.moveToFirst()) {
                    editEquipe1.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EQ1)));
                    editEquipe2.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_EQ2)));
                    editDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE)));
                    editStade.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_STADE)));
                }

                cursor.close();
            }
        });
    }

    // CREATE
    private void ajouterMatch() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_EQ1, editEquipe1.getText().toString());
        values.put(DatabaseHelper.COL_EQ2, editEquipe2.getText().toString());
        values.put(DatabaseHelper.COL_DATE, editDate.getText().toString());
        values.put(DatabaseHelper.COL_STADE, editStade.getText().toString());

        db.insert(DatabaseHelper.TABLE_MATCH, null, values);

        editEquipe1.setText("");
        editEquipe2.setText("");
        editDate.setText("");
        editStade.setText("");
    }

    // UPDATE
    private void modifierMatch() {
        if (selectedId == -1) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.COL_EQ1, editEquipe1.getText().toString());
        values.put(DatabaseHelper.COL_EQ2, editEquipe2.getText().toString());
        values.put(DatabaseHelper.COL_DATE, editDate.getText().toString());
        values.put(DatabaseHelper.COL_STADE, editStade.getText().toString());

        db.update(
                DatabaseHelper.TABLE_MATCH,
                values,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(selectedId)}
        );

        selectedId = -1;
    }

    // DELETE
    private void supprimerMatch() {
        if (selectedId == -1) return;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(
                DatabaseHelper.TABLE_MATCH,
                DatabaseHelper.COL_ID + "=?",
                new String[]{String.valueOf(selectedId)}
        );

        selectedId = -1;
    }

    // READ
    private void afficherMatchs() {
        ids.clear();
        matchs.clear();

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id, equipe1, equipe2, date_match, stade FROM match_afrique",
                null
        );

        while (cursor.moveToNext()) {
            ids.add(cursor.getInt(0));

            String ligne =
                    cursor.getString(1) + " vs " + cursor.getString(2) + "\n" +
                            "📅 " + cursor.getString(3) + " | 🏟 " + cursor.getString(4);

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
