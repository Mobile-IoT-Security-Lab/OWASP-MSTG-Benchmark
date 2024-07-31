package com.example.mastg_test0020;

import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebViewActivity extends AppCompatActivity {
    private WebView view;
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
        Toolbar toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view= findViewById(R.id.web);
        view.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //Ignore TLS certificate errors and instruct the WebViewClient to load the website
                handler.proceed();
            }
        });
        EditText u= findViewById(R.id.editTextText);
        Button s= findViewById(R.id.button2);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().trim().equals("")){
                    Toast.makeText(WebViewActivity.this, "Search Something", Toast.LENGTH_SHORT).show();                }
                else {
                    new HttpGetRequest().execute(u.getText().toString().trim());

                }
            }
        });
    }
    private class HttpGetRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = null;
            String site=null;
            int responseCode = 0;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String inputLine;
                    site=params[0];

                    while ((inputLine = in.readLine()) != null) {
                        responseBuilder.append(inputLine);
                    }

                    in.close();
                    response = responseBuilder.toString();
                }

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return site;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                view.loadUrl(result);
            }
        }
    }
}