package com.example.infomovie.Sqlite;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "database-book-final";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "book_final";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NIM = "nim";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_IS_LOGGED_IN = "isLoggedIn";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NIM + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0)");
    }

    public void insertData(String nim, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NIM, nim);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_IS_LOGGED_IN, 0);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

//    public void updateRecord(int id, int phone, String address) {
//        SQLiteDatabase db = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
//    }

    public void deleteRecords(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getColumnId() {
        return COLUMN_ID;
    }

    public String getColumnNim() {
        return COLUMN_NIM;
    }

}



