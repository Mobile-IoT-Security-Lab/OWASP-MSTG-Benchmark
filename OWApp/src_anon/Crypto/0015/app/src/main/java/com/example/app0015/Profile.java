package com.example.app0015;

import static com.example.app0015.CryptoUtil.encrypt;
import static com.example.app0015.CryptoUtil.getStaticKey;

import android.content.Context;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Profile extends AppCompatActivity {

    @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_profile);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        Toolbar toolbar = findViewById(R.id.bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            EditText pwd= findViewById(R.id.textPwd);
            Button save= findViewById(R.id.enc);
            TextView showContent= findViewById(R.id.content);
            showContent.setText(readFileContent("pwd.txt"));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enc_pwd= null;
                try {
                    enc_pwd = encrypt(pwd.getText().toString()+"\n",getStaticKey());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                saveStringToFile(enc_pwd+"\n");
                showContent.append(enc_pwd+"\n");
            }
        });
    }


    private void saveStringToFile(String text) {
        String fileName="pwd.txt";
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                if (file.createNewFile()) {
                    Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();                } else {
                    Toast.makeText(this, "Impossible to create the file", Toast.LENGTH_SHORT).show();                }
            } else {
                Toast.makeText(this, "File already existing", Toast.LENGTH_SHORT).show();            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName, Context.MODE_APPEND);
            fos.write(text.getBytes());
            Toast.makeText(this, "Pwd saved successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving Pwd", Toast.LENGTH_SHORT).show();
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

    private String readFileContent(String fileName) {
        FileInputStream fis = null;
        StringBuilder content = new StringBuilder();
        try {
            fis = openFileInput(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
}


