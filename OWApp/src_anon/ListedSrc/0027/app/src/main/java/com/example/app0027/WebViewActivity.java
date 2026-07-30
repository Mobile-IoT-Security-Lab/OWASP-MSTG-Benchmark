package com.example.app0027;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayInputStream;

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
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        WebView webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewActivity.CustomWebViewClient());
        webView.loadUrl("https://wwww.example.com");
        WebView webView2 = findViewById(R.id.webview2);
        webView2.setWebViewClient(new WebViewActivity.CustomWebViewClient());
        webView2.loadUrl("https://www.malicious.com");
    }
    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // Handle all URL loading within the WebView itself
            view.loadUrl(url);
            return true;
        }
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();

            if (url.contains("malicious")) {
                String mimeType = "text/html";
                String encoding = "UTF-8";
                String responseContent = "<html><body><h1>Blocked Content</h1></body></html>";

                // Returning a crafted response without any validation or logging
                return new WebResourceResponse(mimeType, encoding, new ByteArrayInputStream(responseContent.getBytes()));
            }

            // If not intercepted, let the WebView handle the request as usual
            return super.shouldInterceptRequest(view, request);
        }
    }

}
