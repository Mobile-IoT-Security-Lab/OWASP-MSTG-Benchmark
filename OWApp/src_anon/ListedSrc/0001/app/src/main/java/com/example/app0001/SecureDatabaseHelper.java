package com.example.app0001;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;



public class SecureDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "secure_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_KEY = "secret"; // Hard-coded key (not recommended)

    public SecureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase.loadLibs(context); // Load SQLCipher libraries

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE my_secure_table ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS my_secure_table");
        onCreate(db);
    }

    // Method to get a writable database using the hard-coded key
    public SQLiteDatabase getSecureWritableDatabase() {
        return getWritableDatabase(DATABASE_KEY);
    }

    public boolean insertData(String name) {
        SQLiteDatabase db = getSecureWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        // Insert data and get the row ID
        long rowId = db.insert("my_secure_table", null, values);

        // Check if the insertion was successful
        if (rowId != -1) {
            Log.d("", "Data inserted successfully with row ID: " + rowId);
            return true;
        } else {
            Log.e("", "Failed to insert data.");
            return false;
        }
    }
}

