package com.example.mastg_test0015;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

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
        EditText u= findViewById(R.id.editTextText);
        EditText p = findViewById(R.id.editTextTextPassword);
        Button r= findViewById(R.id.button);
        Button l= findViewById(R.id.button3);
        Crypt obj= new Crypt();
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (u.getText().toString().isEmpty() || p.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    appendToFile(MainActivity.this,("Username: "+u.getText().toString()+" Password: "+obj.encryptData(p.getText().toString()))+"\n");
                    Intent myIntent = new Intent(MainActivity.this, Login.class);
                    MainActivity.this.startActivity(myIntent);
                }
            }
        });
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, Login.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

    }
    public static void appendToFile(Context context, String data) {
        FileOutputStream fos = null;
        try {
            // Apre il file in modalità append, se non esiste verrà creato
            fos = context.openFileOutput("credentials.txt", Context.MODE_APPEND);
            // Scrive i dati nel file
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
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