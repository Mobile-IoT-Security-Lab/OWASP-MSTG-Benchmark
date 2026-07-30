package com.example.app0017;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
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

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class Profile extends AppCompatActivity {


    private static final String KEY_ALIAS = "MySecretKey";
    private static final String TRANSFORMATION = "AES/CBC/PKCS7Padding";
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
        try {
            generateSecretKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
        EditText nota = findViewById(R.id.note);
        Button saveNota = findViewById(R.id.save);
        String txt = getTextFromFile(Profile.this);
        TextView savedResults = findViewById(R.id.textView7);
        savedResults.append(txt + "\n");

        saveNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nota.getText().toString().trim().equals("")) {
                    Toast.makeText(Profile.this, "Fill the form", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        SecretKey secretKey = getSecretKey();  // Recupera la chiave dal KeyStore
                        saveTextToFile(Profile.this, encryptText(nota.getText().toString() ,secretKey)+ "\n");
                        savedResults.append(encryptText(nota.getText().toString() ,secretKey)+ "\n");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
    // Recupera la chiave segreta dal KeyStore
    private SecretKey getSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);
        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }

    public static String getTextFromFile(Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        FileInputStream fileInputStream;
        try {
            // Verifica se il file esiste
            File file = new File(context.getFilesDir(), "note.txt");
            if (!file.exists()) {
                // Crea il file se non esiste
                file.createNewFile();

            }
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
    private void generateSecretKey() throws Exception {
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                // BAD: User authentication is not required to use this key.
                .setUserAuthenticationRequired(false)
                .build();

        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    // Cifra una stringa utilizzando la chiave segreta
    private String encryptText(String plainText, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] iv = cipher.getIV();  // Inizializzazione vettore (IV)
        byte[] encryption = cipher.doFinal(plainText.getBytes("UTF-8"));

        // Concatena IV e il testo cifrato per facilitare la decodifica
        byte[] encryptedData = new byte[iv.length + encryption.length];
        System.arraycopy(iv, 0, encryptedData, 0, iv.length);
        System.arraycopy(encryption, 0, encryptedData, iv.length, encryption.length);

        // Codifica in Base64 per una rappresentazione sicura come stringa
        return Base64.encodeToString(encryptedData, Base64.DEFAULT);
    }


}