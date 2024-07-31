package com.example.mastg_test0011;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Note extends AppCompatActivity {
    private String encryptionKey;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String PREF_NAME = "MyPrefs";
    private static final String TEXT_KEY = "savedText";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
 
        EditText nota= findViewById(R.id.note);
        Button saveNota= findViewById(R.id.save);
        String txt=getTextFromFile(this);
        TextView savedResults= findViewById(R.id.textView7);
        savedResults.append(txt+"\n");

        saveNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nota.getText().toString().equals("")){
                    Toast.makeText(Note.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else{
                    try {
                        encryptAndSave(nota.getText().toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        Button log=findViewById(R.id.out);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }
    private String generateEncryptionKey() {
        return "Talos";
    }
    private void logout() {
        // Vulnerability 7: Not clearing sensitive data on sign out
        // Simulating sign out
        this.encryptionKey = null; // Not properly clearing sensitive data from memory
        Intent intent = new Intent(Note.this, MainActivity.class);
        startActivity(intent);
    }
    public static String encryptNote(String text, String key) {
        // Create a mapping between the alphabet and the key
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder encryptedText = new StringBuilder();

        for (char c : text.toCharArray()) {
            int index = alphabet.indexOf(Character.toLowerCase(c));
            if (index != -1) {
                char encryptedChar = key.charAt(index % key.length());
                encryptedChar = Character.isUpperCase(c) ? Character.toUpperCase(encryptedChar) : encryptedChar;
                encryptedText.append(encryptedChar);
            } else {
                encryptedText.append(c);  // Keep non-alphabetic characters unchanged
            }
        }

        return encryptedText.toString();
    }
    private void encryptAndSave(String note) throws Exception {
        // Vulnerability 4: Not overwriting sensitive data
        this.encryptionKey = generateEncryptionKey();
        String res=encryptNote(note,this.encryptionKey);
        TextView x= findViewById(R.id.textView7);
        x.append(res+"\n");
        Log.d( "encryptAndSave: ",""+res);
        saveTextToFile(this,res+"\n");
        // Perform encryption using this.encryptionKey
    }
    public static void saveTextToFile(Context context, String text) {
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput("note.txt", Context.MODE_APPEND);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getTextFromFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream;

        try {
            fileInputStream = context.openFileInput("note.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append(",\n");
            }

            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}