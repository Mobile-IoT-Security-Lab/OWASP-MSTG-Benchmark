package com.example.mastg_test0041;

import static android.content.ContentValues.TAG;

import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
        // Enable StrictMode for testing purposes (should not be used in production)

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .detectNetwork()  // Detect network operations on main thread
                .penaltyLog()     // Log violations to LogCat
                .build();
        StrictMode.setThreadPolicy(policy);


        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        EditText u= findViewById(R.id.editTextText);
        Button b= findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        URL url = new URL(u.getText().toString()); // Replace with a valid URL
                        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                        int responseCode = 0;

                            responseCode = urlConnection.getResponseCode();

                        urlConnection.disconnect();
                        if (responseCode == 200){
                            webView.loadUrl(url.toString());
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
}