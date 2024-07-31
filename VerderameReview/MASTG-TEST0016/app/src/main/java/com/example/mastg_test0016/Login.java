package com.example.mastg_test0016;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class Login extends AppCompatActivity {
    private static final String SESSION_PREF_NAME = "SessionPrefs";
    private static final String KEY_SESSION_TOKEN = "sessionToken";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
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
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sharedPreferences = getSharedPreferences(SESSION_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        EditText text1= findViewById(R.id.editTextText2);
        EditText text2= findViewById(R.id.editTextTextPassword2);
        Button login= findViewById(R.id.button2);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u= text1.getText().toString();
                String p= text2.getText().toString();
                int duration = Toast.LENGTH_SHORT;
                boolean result= checkCredentials(u,p);
                Log.d("result func:", ""+result);
                if (!result){
                    Toast.makeText(Login.this, "Wrong Credential", duration).show();
                }
                else {
                    createSession();
                    Intent intent = new Intent(Login.this, Profile.class);
                    startActivity(intent);
                }
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
    // Method to create a new session and assign the generated token
    public void createSession() {
        String sessionToken = generateSessionToken(); // Assume you have a method to generate session token
        editor.putString(KEY_SESSION_TOKEN, sessionToken);
        editor.apply();
    }
    // Method to get the session token
    public String getSessionToken() {
        return sharedPreferences.getString(KEY_SESSION_TOKEN, null);
    }
    // Method to generate session token
    private String generateSessionToken() {
        // Initialize random number generator
        Random random = new Random();

        // Define characters to use for session token generation
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        // Length of session token
        int tokenLength = 16;

        // StringBuilder to store generated session token
        StringBuilder sessionToken = new StringBuilder(tokenLength);

        // Generate random session token
        for (int i = 0; i < tokenLength; i++) {
            // Get random index to select character from characters string
            int randomIndex = random.nextInt(characters.length());
            // Append randomly selected character to session token
            sessionToken.append(characters.charAt(randomIndex));
        }

        // Convert StringBuilder to String and return session token
        return sessionToken.toString();
    }
}


