package com.example.mastg_test0015;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText e= findViewById(R.id.enc);
        EditText d= findViewById(R.id.dec);
        TextView resE=findViewById(R.id.encRes);
        TextView resD=findViewById(R.id.decRes);
        Button encB=findViewById(R.id.encButton);
        Button decB= findViewById(R.id.decButton);
        Crypt obj= new Crypt();
        encB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e.getText().toString().isEmpty()){
                    Toast.makeText(Profile.this, "Fill the Input", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("TAG", "onClick: "+obj.encryptData(e.getText().toString()));
                    resE.setText(obj.encryptData(e.getText().toString()));
                }
            }
        });
        decB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (e.getText().toString().isEmpty()){
                    Toast.makeText(Profile.this, "Fill the Input", Toast.LENGTH_SHORT).show();
                }
                else {
                    resD.setText(obj.decryptData(d.getText().toString().getBytes()));
                }
            }
        });
    }


}