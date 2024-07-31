package com.example.mastg_test0039;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i=0;i<10;i++){
            saveCredentialsToFile(generateUsername(),generatePassword());
        }

        EditText text1 = findViewById(R.id.editTextText);
        EditText text2 = findViewById(R.id.editTextTextPassword);
        Button login = findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = text1.getText().toString();
                String p = text2.getText().toString();
                if (u.equals("") || p.equals("")) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivity.this, "Fill the Form", duration).show();
                    return;
                }
                int duration = Toast.LENGTH_SHORT;
                boolean result= checkCredentials(u,p);
                Log.d("result func:", ""+result);
                if (!result){
                    Toast.makeText(MainActivity.this, "Wrong Credential", duration).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, Profile.class);
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
    private void saveCredentialsToFile(String username, String password) {
        FileOutputStream fos = null;
        try {
            // Open the file in append mode
            fos = openFileOutput("credentials.txt", MODE_APPEND);
            String data = "Username: " + username + " Password: " + password + "\n"; // Add newline for separation
            Log.d("Credentials", ""+data);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Error saving credentials", Toast.LENGTH_SHORT).show();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String generateUsername() {
        Random random = new Random();
        int randomNumber = random.nextInt(10000); // Generate a random number between 0 and 9999
        String username = "user_" + randomNumber;
        return username;
    }

    // Function to generate a random password
    public static String generatePassword() {
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCaseLetters = upperCaseLetters.toLowerCase();
        String numbers = "0123456789";
        String characters = upperCaseLetters + lowerCaseLetters + numbers;

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        // Generate a password of length 8
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }

        return password.toString();
    }
}