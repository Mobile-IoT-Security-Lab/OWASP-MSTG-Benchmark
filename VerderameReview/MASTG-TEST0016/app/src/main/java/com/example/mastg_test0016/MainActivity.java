package com.example.mastg_test0016;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public int randomNumberGenerator() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        Log.d("Random Number: ", "" + randomNumber);
        return randomNumber;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText text1 = findViewById(R.id.editTextText);
        EditText text2 = findViewById(R.id.editTextTextPassword);
        Button register = findViewById(R.id.button);
        Button login = findViewById(R.id.lg);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = text1.getText().toString();
                String p = text2.getText().toString();
                if (u.equals("") || p.equals("")) {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivity.this, "Fill the Form", duration).show();
                    return;
                }
                saveCredentialsToFile(u, p);
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
            }
        });
    }
    private void saveCredentialsToFile(String username, String password) {
        FileOutputStream fos = null;
        try {
            // Open the file in append mode
            fos = openFileOutput("credentials.txt", MODE_APPEND);
            String data = "Username: " + username + " Password: " + password + "\n"; // Add newline for separation
            fos.write(data.getBytes());
            Toast.makeText(MainActivity.this, "Credentials saved to file", Toast.LENGTH_SHORT).show();
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
}