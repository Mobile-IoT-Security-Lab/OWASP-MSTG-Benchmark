package com.example.mastg_test_0003;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Simulating the logging of sensitive data (e.g., private key)
        EditText username= findViewById(R.id.editTextText);
        EditText pwd= findViewById(R.id.editTextTextPassword);
        Button login= findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Username: ",""+username.getText().toString());
                Log.d("Password: ",""+pwd.getText().toString());
                Intent intent = new Intent(MainActivity.this, Activity2.class);
                intent.putExtra("Username", username.getText().toString()); // Opzionale: passa dati extra
                startActivity(intent);
            }
        });
        String privateKey = "my_secret_private_key";
        Log.d("PWD", "Password: "+pwd);
        Log.v("SECRET", "Private key: " + privateKey);
        // Simulating the logging of sensitive data (e.g., private key)
        Log.d("SECRET", "Private key: " + privateKey);
    }
}