package com.example.mastg_test0002;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private void login(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Retrieve the value associated with the key
        String value = sharedPreferences.getString(KEY_USERNAME,null);
        if (username.equals("admin")) {
            sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            intent.putExtra("Username", username); // Opzionale: passa dati extra
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "You did not enter the correct username", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    private SharedPreferences sharedPreferences;
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
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(KEY_USERNAME,null);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if(!isLoggedIn) {
            sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(KEY_USERNAME, "admin");
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();
            Button event = findViewById(R.id.button);
            event.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText user = findViewById(R.id.editTextText);
                    String check = user.getText().toString();
                    if (check.matches("") || !check.equals("admin")) {
                        Toast.makeText(MainActivity.this, "You did not enter a username", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    login(user.getText().toString());

                }
            });

        }
        else {
            Intent intent = new Intent(MainActivity.this, Activity2.class);
            intent.putExtra("Username", value); // Opzionale: passa dati extra
            startActivity(intent);
        }

    }
}
