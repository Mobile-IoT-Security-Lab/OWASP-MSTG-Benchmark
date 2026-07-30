package com.example.app0015;

import static com.example.app0015.CryptoUtil.encrypt;
import static com.example.app0015.CryptoUtil.getStaticKey;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button event1= findViewById(R.id.button);
        EditText u= findViewById(R.id.editTextText);
        EditText p= findViewById(R.id.editTextText2);

        event1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().trim().equals("")||p.getText().toString().trim().equals("")){
                    Toast.makeText(MainActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        if (checkCredentials(u.getText().toString(),encrypt(p.getText().toString(),getStaticKey()))) {
                            Intent intent = new Intent(MainActivity.this, Profile.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Wrong Credential", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        });
        TextView registration= findViewById(R.id.linkRegistration);
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
    }


    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = openFileInput("credentials.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line into username and password parts
                String[] parts = line.split(" ");
                if (parts.length == 4 && parts[0].equals("Username:") && parts[2].equals("Password:")) {
                    String storedUsername = parts[1];
                    String storedPassword = parts[3];
                    // Remove any trailing spaces
                    storedUsername = storedUsername.trim();
                    storedPassword = storedPassword.trim();
                    if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                        return true; // Credentials match
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false; // Credentials not found or error occurred
    }
}