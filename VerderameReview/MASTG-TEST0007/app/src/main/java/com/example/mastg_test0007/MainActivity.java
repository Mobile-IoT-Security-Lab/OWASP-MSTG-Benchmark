package com.example.mastg_test0007;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
        EditText u = findViewById(R.id.editTextText);
        EditText p = findViewById(R.id.editTextTextPassword);
        Button r = findViewById(R.id.button);
        Button l = findViewById(R.id.button2);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!u.getText().toString().isEmpty() || !p.getText().toString().isEmpty()) {
                    // class to add values in the database
                    ContentValues values = new ContentValues();

                    // fetching text from user
                    values.put(MyContentProvider.name, u.getText().toString());
                    values.put(MyContentProvider.pwd, p.getText().toString());

                    // inserting into database through content URI
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);

                    // displaying a toast message
                    Toast.makeText(getBaseContext(), "New Record Inserted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Fill the Form", Toast.LENGTH_LONG).show();

                }
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser(view, u.getText().toString(), p.getText().toString());
            }
        });
    }


    //INSECURE LOGIN
    public void checkUser(View view, String user, String pwd) {
        // Define the selection criteria
        String selection = "name = '"+user+ "' AND pwd ='"+pwd+"'";

// Perform the query using ContentResolver
        Cursor cursor = getContentResolver().query(
                Uri.parse("content://com.example.mastg_test0007.provider/users"), // Content URI
                null,           // Projection (null means all columns)
                selection,      // Selection criteria
                null,  // Selection arguments
                null            // Sort order (null means no sorting)
        );
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        // Retrieve data from the cursor
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String name_db = cursor.getString(cursor.getColumnIndex("name"));
                        String pwd_db = cursor.getString(cursor.getColumnIndex("pwd"));

                        // Process the retrieved data as needed
                        Log.d("User", "ID: " + id + ", Name: " + name_db + ", Password: " + pwd_db);
                        Toast.makeText(MainActivity.this, "Correct!", Toast.LENGTH_LONG).show();
                    } while (cursor.moveToNext());
                } else {
                    // User does not exist, display a toast
                    Toast.makeText(MainActivity.this, "User does not exist or incorrect credentials", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e){
                Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_LONG).show();
            } finally {
                cursor.close(); // Close the cursor when done
            }

    }
    }
}