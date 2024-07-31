package com.example.mastg_test_0006;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText u= findViewById(R.id.editTextText);
        EditText p= findViewById(R.id.editTextTextPassword);
        u.setInputType(InputType.TYPE_CLASS_TEXT);
        p.setInputType(InputType.TYPE_CLASS_TEXT);
        Button login= findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !u.getText().toString().equals("") || !p.getText().toString().equals("")) {
                    Intent intent = new Intent(MainActivity.this, Activity2.class);
                    intent.putExtra("Username", u.getText().toString()); // Opzionale: passa dati extra
                    startActivity(intent);
                }
                else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivity.this, "Fill the form!", duration).show();

                }
            }
        });


    }
}