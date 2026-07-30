package com.example.app0013;

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

import org.w3c.dom.Text;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Decrypt extends AppCompatActivity {
    private static byte[] keyBytesAES ;

    private static byte[] keyBytesDES ;
    static{
       Decrypt.keyBytesDES="12345678".getBytes();

        Decrypt.keyBytesAES= new byte[] {7, 3, 4, 5, 6, 7, 8, 9, 16, 17, 18, 9, 20, 21, 15, 1};
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_decrypt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText dc= findViewById(R.id.editTextText2);
        EditText textDes= findViewById(R.id.textdes);
        TextView resDec= findViewById(R.id.decResult);
        TextView resDes= findViewById(R.id.desRes);
        Button decB= findViewById(R.id.button3);
        decB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dc.getText().toString().equals("")){
                    Toast.makeText(Decrypt.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    resDec.setText(decryptDataAES(dc.getText().toString().getBytes()));
                }
            }
        });
        Button decDes=findViewById(R.id.DES);
        decDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textDes.getText().toString().equals("")){
                    Toast.makeText(Decrypt.this, "Fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("PIPPOOOOO", "onClick: ");
                    resDes.setText(decryptDataDES(textDes.getText().toString().getBytes()));
                }
            }
        });
    }
    private String decryptDataAES(byte[] encryptedData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(Decrypt.keyBytesAES, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            return new String(cipher.doFinal(Base64.decode(encryptedData,0)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String decryptDataDES(byte[] encryptedData) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytesDES, "DES");

            Cipher cipher = Cipher.getInstance("DES");

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            return new String(cipher.doFinal(Base64.decode(encryptedData, Base64.DEFAULT)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}