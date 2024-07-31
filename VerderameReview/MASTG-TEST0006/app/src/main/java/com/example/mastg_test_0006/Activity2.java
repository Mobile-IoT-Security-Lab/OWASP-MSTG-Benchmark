package com.example.mastg_test_0006;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Activity2 extends AppCompatActivity {

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
        Intent intent = getIntent();
        String User;
        TextView view= findViewById(R.id.textView);
// Controlla se ci sono dati extra associati a questo intent
        if (intent != null) {
            // Recupera i dati extra tramite il metodo getIntent().getExtras()
            Bundle extras = intent.getExtras();
            // Controlla se ci sono dati extra e recuperali utilizzando la chiave specificata
            if (extras != null) {
                String user = extras.getString("Username");
                view.setText("Welcome "+user+"!");
                WebView v= findViewById(R.id.view);
                v.loadUrl("https://www.example.com");
            }
        }
    }
}