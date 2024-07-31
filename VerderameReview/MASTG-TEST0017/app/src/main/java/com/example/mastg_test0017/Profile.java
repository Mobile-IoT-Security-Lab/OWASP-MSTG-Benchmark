package com.example.mastg_test0017;

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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Profile extends AppCompatActivity {
    private static final String KEY_USERNAME = "username";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String PREF_NAME = "MyPrefs";
    private static final String CARD_NUMBER="1234567890";
    private static final String CARD_PIN="1234";
    private SharedPreferences sharedPreferences;
    private static boolean flag= false;


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
        Button logOut= findViewById(R.id.button2);
        Button credential= findViewById(R.id.button3);
        TextView cn=findViewById(R.id.cn1);
        TextView pin=findViewById(R.id.pin1);
        String encCn=encrypt(CARD_NUMBER);
        String encPin=encrypt(CARD_PIN);
        cn.setText(encCn);
        pin.setText(encPin);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String name = intent.getStringExtra("user");
                Log.d("USERNAME", ""+name);
                logout(name);
            }
        });
        credential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=!flag;
                if (flag) {
                    cn.setText(CARD_NUMBER);
                    pin.setText(CARD_PIN);
                }
                else{
                    cn.setText(encCn);
                    pin.setText(encPin);
                }
            }
        });
    }
    public static String encrypt(String input) {
        try {
            // Creating MD5 Hash
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String md5Hash = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (md5Hash.length() < 32) {
                md5Hash = "0" + md5Hash;
            }
            return md5Hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception according to your requirement
            return null;
        }
    }

    private void logout(String username) {
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
    }
}