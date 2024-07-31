package com.example.mastg_test0029;

import android.content.Context;
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

public class PwdManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pwd_manager);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText d= findViewById(R.id.editTextText2);
        EditText p= findViewById(R.id.editTextText3);
        Button s= findViewById(R.id.save);
        TextView list= findViewById(R.id.pwds);
        list.setText(readFileContent(this));
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (d.getText().toString().isEmpty() || p.getText().toString().isEmpty()){
                    Toast.makeText(PwdManager.this, "Fill The form", Toast.LENGTH_LONG).show();
                }
                else {
                    appendTextToFile(getApplicationContext(),d.getText().toString(),p.getText().toString());
                    list.append(d.getText().toString()+": "+p.getText().toString()+"\n");
                }
            }
        });
    }
    public static void appendTextToFile(Context context, String domain, String pwd) {
        FileOutputStream fos = null;
        try {
            // Open the file in append mode
            fos = context.openFileOutput("pwd.txt", Context.MODE_APPEND);
            fos.write((domain+": "+pwd).getBytes());
            fos.write("\n".getBytes()); // Add a newline after each append (optional)
            Log.d("FileUtil", "Text appended to file: " );
            Toast.makeText(context.getApplicationContext(), "Pwd Saved", Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "Error appending text to file: " );
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
    public static String readFileContent(Context context) {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuilder content = new StringBuilder();

        try {
            fis = context.openFileInput("pwd.txt");
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileUtil", "Error reading file: pwd.txt");
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content.toString();
    }}