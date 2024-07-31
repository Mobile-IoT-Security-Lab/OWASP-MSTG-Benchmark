package com.example.mastg_test0013;

import android.content.Intent;
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
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    private static byte[] keyBytes ;
    static{
        MainActivity.keyBytes= new byte[] {7, 3, 4, 5, 6, 7, 8, 9, 16, 17, 18, 9, 20, 21, 15, 1};

    }


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
        EditText encrypt= findViewById(R.id.enc);
        Button encB=findViewById(R.id.button);
        TextView res= findViewById(R.id.encResult);
        encB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (encrypt.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    res.setText(encryptData(encrypt.getText().toString()));
                }
            }
        });
        Button goToDec= findViewById(R.id.button2);
        goToDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Decrypt.class);
                startActivity(intent);
            }
        });
    }
    private CharSequence encryptData(String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(MainActivity.keyBytes, "AES"); // Hardcoded key
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            return new String(Base64.encode(cipher.doFinal(data.getBytes()),0));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}