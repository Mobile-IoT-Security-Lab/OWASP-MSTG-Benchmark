package com.example.mastg_test_0003;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
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
        Intent intent = getIntent();
        String User;
// Controlla se ci sono dati extra associati a questo intent
        if (intent != null) {
            // Recupera i dati extra tramite il metodo getIntent().getExtras()
            Bundle extras = intent.getExtras();
            // Controlla se ci sono dati extra e recuperali utilizzando la chiave specificata
            if (extras != null) {
                String user = extras.getString("Username");
                TextView logged= findViewById(R.id.textView);
                logged.setText("Welcome "+user+"!");
                WebView view= findViewById(R.id.webV);

                view.loadUrl("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
            }
        }
    }
}