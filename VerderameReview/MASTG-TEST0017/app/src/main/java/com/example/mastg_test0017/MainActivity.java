package com.example.mastg_test0017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "MyPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final int duration= Toast.LENGTH_SHORT;



    private void login(String username) {
        String user = sharedPreferences.getString(KEY_USERNAME, null);
        if (user.equals(username)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.apply();
            Intent intent = new Intent(MainActivity.this, Profile.class);
            intent.putExtra("user", username);
            startActivity(intent);
        }
        else {
            Toast.makeText(MainActivity.this, "Wrong Username", duration).show();

        }
    }

    private boolean sharedPreferenceCheck(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String user = sharedPreferences.getString(KEY_USERNAME, null);
        Log.d("Username: ",""+user);
        if (user==null){
            editor.putString(KEY_USERNAME, "admin");
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();
            return false;
        }
        return true;
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
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        sharedPreferenceCheck();
        TextView log= findViewById(R.id.textView2);
        Button event= findViewById(R.id.button);
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText user= findViewById(R.id.editTextText);
                if (user.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Fill the Form", duration).show();
                    return;
                }
                login(user.getText().toString());

            }
        });

        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
        if (isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, Profile.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Please login", Toast.LENGTH_SHORT).show();
        }

    }

}