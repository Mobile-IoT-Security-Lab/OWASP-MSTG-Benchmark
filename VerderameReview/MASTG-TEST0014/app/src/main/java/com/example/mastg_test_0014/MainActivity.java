package com.example.mastg_test_0014;

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

import androidx.appcompat.app.AppCompatActivity;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button event1= findViewById(R.id.button);
        EditText u= findViewById(R.id.editTextText);
        EditText p= findViewById(R.id.editTextText2);
        String User;
        String Pwd;
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        // Check if preferences are empty
        if (sharedPreferences.getAll().isEmpty()) {
            // If preferences are empty, insert username and password
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", "Admin");
            editor.putString("password", "1234");
            editor.apply();
        }
        event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().equals("")||p.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    String user=sharedPreferences.getString("username", "");
                    String password=sharedPreferences.getString("password", "");
                    if (user.equals(u.getText().toString()) && password.equals(p.getText().toString())) {
                        Intent intent = new Intent(MainActivity.this, Profile.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Wrong Credential", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
}