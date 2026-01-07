package com.firstapp.exam;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "can.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_MATCH = "match_afrique";
    public static final String COL_ID = "id";
    public static final String COL_EQ1 = "equipe1";
    public static final String COL_EQ2 = "equipe2";
    public static final String COL_DATE = "date_match";
    public static final String COL_STADE = "stade";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_MATCH + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_EQ1 + " TEXT, " +
                COL_EQ2 + " TEXT, " +
                COL_DATE + " TEXT, " +
                COL_STADE + " TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MATCH);
        onCreate(db);
    }
}
