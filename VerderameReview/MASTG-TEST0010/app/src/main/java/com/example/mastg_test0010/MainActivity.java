package com.example.mastg_test0010;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText text1= findViewById(R.id.editTextText);
        EditText text2= findViewById(R.id.editTextText2);
        Button login= findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u= text1.getText().toString();
                String p= text2.getText().toString();
                if (u.equals("")||p.equals("")){
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivity.this, "Fill the form", duration).show();
                }
                else{
                    Intent intent = new Intent(MainActivity.this, Profile.class);
                    intent.putExtra("Username", u); // Opzionale: passa dati extra
                    intent.putExtra("Password",p);
                    startActivity(intent);
                }
            }
        });
    }
}