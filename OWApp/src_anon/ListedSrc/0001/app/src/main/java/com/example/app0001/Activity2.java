package com.example.app0001;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {
    private static final String PREF_NAME = "MyPrefs";

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
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SecureDatabaseHelper db= new SecureDatabaseHelper(this);
        Intent intent = getIntent();
        String User;
// Controlla se ci sono dati extra associati a questo intent
        if (intent != null) {
            // Recupera i dati extra tramite il metodo getIntent().getExtras()
            Bundle extras = intent.getExtras();
            // Controlla se ci sono dati extra e recuperali utilizzando la chiave specificata
            if (extras != null) {
                String user = extras.getString("Username");
                SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_WORLD_READABLE);
                String Sp = sharedPreferences.getString(user, "");
                TextView view= findViewById(R.id.textView);
                view.setText("Username: "+user+" Password: "+Sp);
            }
        }

        EditText secret = findViewById(R.id.secret);
        Button sndDB= findViewById(R.id.sendToDB);
        sndDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean res=db.insertData(secret.getText().toString());
                if (res)
                    Toast.makeText(Activity2.this, "Secret Inserted Correctly", Toast.LENGTH_LONG).show();
                else Toast.makeText(Activity2.this, "Error", Toast.LENGTH_LONG).show();

            }
        });

    }
}