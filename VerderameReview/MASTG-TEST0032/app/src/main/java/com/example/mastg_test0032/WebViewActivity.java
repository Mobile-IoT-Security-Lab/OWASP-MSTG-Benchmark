package com.example.mastg_test0032;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_web_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        WebView webView= findViewById(R.id.webView);
        // Load a local HTML file from external storage (vulnerable practice)
        // Enable unsafe WebView settings (vulnerable configurations)
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        settings.setAllowFileAccess(true);  // Enabling file system access
        settings.setAllowFileAccessFromFileURLs(true);  // Allowing file access from file URLs
        settings.setAllowUniversalAccessFromFileURLs(true);  // Allowing universal access from file URLs
        settings.setAllowContentAccess(true);  // Enabling content access
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d(TAG, "No SDCARD");
        } else {
            webView.loadUrl("file:///sdcard/Android/data/home.html");
        }


    }
}