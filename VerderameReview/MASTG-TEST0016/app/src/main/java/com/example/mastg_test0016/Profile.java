package com.example.mastg_test0016;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Profile extends AppCompatActivity {
    private static final String KEY_SESSION_TOKEN = "sessionToken";
    private static final String SESSION_PREF_NAME = "SessionPrefs";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        sharedPreferences = getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Button out= findViewById(R.id.button3);
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Method to clear the session (logout)
                clearSession();
            }
        });
    }
    public void clearSession() {
        editor.remove(KEY_SESSION_TOKEN);
        editor.apply();
    }
}