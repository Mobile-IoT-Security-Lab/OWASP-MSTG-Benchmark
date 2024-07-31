package com.example.mastg_test0031;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.v1);
        EditText text = findViewById(R.id.editTextText);
        Button surf= findViewById(R.id.search);
        webView.getSettings().setJavaScriptEnabled(true);

        surf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().equals("")){
                    Toast toast = Toast.makeText(MainActivity.this /* MyActivity */, "Fill the Input!", Toast.LENGTH_SHORT);

                }
                else {
                    String val = text.getText().toString();
                    webView.loadUrl(val);
                }
            }
        });


    }
}