package com.example.mastg_test0015;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText u=findViewById(R.id.editTextText2);
        EditText p= findViewById(R.id.editTextTextPassword2);
        Button l= findViewById(R.id.button2);
        Crypt obj= new Crypt();
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u.getText().toString().isEmpty() || p.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (checkCredentials(u.getText().toString(),obj.encryptData(p.getText().toString()))) {
                        Intent myIntent = new Intent(Login.this, Profile.class);
                        Login.this.startActivity(myIntent);
                    }
                    else {
                        Toast.makeText(Login.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean checkCredentials(String enteredUsername, String enteredPassword) {
        File file = new File("/data/data/com.example.mastg_test0015/files/credentials.txt");
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(file);
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
                    enteredUsername=enteredUsername.trim();
                    enteredPassword=enteredPassword.trim();
                    Log.d("checkCredentials:", "checkCredentials: "+storedUsername+ " "+storedPassword+ " Entered"+ enteredUsername+ " "+ enteredPassword);
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
