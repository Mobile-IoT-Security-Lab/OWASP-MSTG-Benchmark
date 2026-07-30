package com.example.app0015;

import static com.example.app0015.CryptoUtil.encrypt;
import static com.example.app0015.CryptoUtil.getStaticKey;

import android.content.Intent;
import android.os.Bundle;
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

import java.io.FileOutputStream;
import java.io.IOException;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.RegistrationBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button register = findViewById(R.id.registerButton);
        EditText u = findViewById(R.id.user);
        EditText p = findViewById(R.id.pwd);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().trim().isEmpty() || p.getText().toString().trim().isEmpty())
                    Toast.makeText(Registration.this, "Fill The Form", Toast.LENGTH_LONG).show();
                else {
                    try {
                        saveCredentialsToFile(u.getText().toString(),encrypt(p.getText().toString(),getStaticKey()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Intent intent = new Intent(Registration.this, MainActivity.class);
                    startActivity(intent);
                }
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
                Toast.makeText(Registration.this, "Credentials saved to file", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(Registration.this, "Error saving credentials", Toast.LENGTH_SHORT).show();
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
