package com.example.mastg_test0025;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        try {
            mDB = openOrCreateDatabase("sqli", MODE_PRIVATE, null);
            mDB.execSQL("DROP TABLE IF EXISTS sqliuser;");
            mDB.execSQL("CREATE TABLE IF NOT EXISTS sqliuser(user VARCHAR, password VARCHAR, credit_card VARCHAR);");
            mDB.execSQL("INSERT INTO sqliuser VALUES ('admin', 'passwd123', '1234567812345678');");
            mDB.execSQL("INSERT INTO sqliuser VALUES ('Talos', 'p@ssword123', '1111222233334444');");
            mDB.execSQL("INSERT INTO sqliuser VALUES ('Unige', 'password123', '5555666677778888');");

        }
        catch(Exception e) {
            Log.d("sqli", "Error occurred while creating database for SQLI: " + e.getMessage());
        }
    }
    public void search(View view) {
        EditText srchtxt = (EditText) findViewById(R.id.search);
        EditText pwd = findViewById(R.id.editTextTextPassword);
        if (!srchtxt.getText().toString().matches("") || !pwd.getText().toString().matches("")) {
            Cursor cr = null;
            try {
                cr = mDB.rawQuery("SELECT * FROM sqliuser WHERE user = '" + srchtxt.getText().toString() + "'" + "AND password= '" + pwd.getText().toString() + "'", null);
                StringBuilder strb = new StringBuilder("");
                if ((cr != null) && (cr.getCount() > 0)) {
                    cr.moveToFirst();

                    do {
                        strb.append("User: (" + cr.getString(0) + ") pass: (" + cr.getString(1) + ") Credit card: (" + cr.getString(2) + ")\n");
                    } while (cr.moveToNext());
                    Intent myIntent = new Intent(MainActivity.this, Activity2.class);
                    startActivity(myIntent);

                } else {
                    strb.append("User: (" + srchtxt.getText().toString() + ") not found");
                }
                Toast.makeText(MainActivity.this, strb.toString(), Toast.LENGTH_SHORT).show();


            } catch (Exception e) {
                Log.d("sqli", "Error occurred while searching in database: " + e.getMessage());
            }
        }
        else {
            Toast.makeText(MainActivity.this, "Fill the form!", Toast.LENGTH_SHORT).show();

        }
    }
}
