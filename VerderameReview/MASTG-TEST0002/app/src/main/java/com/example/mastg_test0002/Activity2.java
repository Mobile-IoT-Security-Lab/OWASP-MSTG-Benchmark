package com.example.mastg_test0002;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class Activity2 extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String User;
        TextView view= findViewById(R.id.textView2);
        SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        Intent intent = getIntent();
// Controlla se ci sono dati extra associati a questo intent
        if (intent != null) {
            // Recupera i dati extra tramite il metodo getIntent().getExtras()
            Bundle extras = intent.getExtras();
            // Controlla se ci sono dati extra e recuperali utilizzando la chiave specificata
            if (extras != null) {
                User = extras.getString("Username");
                String Sp = sharedPreferences.getString(User, "");
                view.setText("Welcome "+ User);
            }
        }
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        Button out= findViewById(R.id.button2);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(KEY_USERNAME, view.getText().toString().substring(7));
                editor.putBoolean(KEY_IS_LOGGED_IN, false);
                editor.apply();
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}