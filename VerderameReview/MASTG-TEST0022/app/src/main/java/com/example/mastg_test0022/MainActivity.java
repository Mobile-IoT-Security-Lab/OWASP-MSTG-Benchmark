package com.example.mastg_test0022;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    String apiUrlPinning = "https://www.example.com/";
    String apiUrl="https://publicobject.com/";

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
        Button n=findViewById(R.id.NoPinning);
        Button y = findViewById(R.id.Pinning);
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MakeGetRequest(new MakeGetRequest.OnRequestListener() {
                    @Override
                    public void onSuccess(String response) {
                        // Handle successful response here
                        Log.d("HTTP GET Response", response);
                        Toast.makeText(MainActivity.this, "Success Request without SSL pinning", Toast.LENGTH_SHORT).show();
                        // Now you can use 'response' in your application logic
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error here
                        Log.e("HTTP GET Error", error);
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                    }
                }).execute(apiUrlPinning);
            }
        });



        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Execute AsyncTask to make GET request
                new MakeGetRequest(new MakeGetRequest.OnRequestListener() {
                    @Override
                    public void onSuccess(String response) {
                        // Handle successful response here
                        Log.d("HTTP GET Response", response);
                        Toast.makeText(MainActivity.this, "Success Request without SSL pinning", Toast.LENGTH_SHORT).show();
                        // Now you can use 'response' in your application logic
                    }

                    @Override
                    public void onError(String error) {
                        // Handle error here
                        Log.e("HTTP GET Error", error);
                        Toast.makeText(MainActivity.this, "ERROR", Toast.LENGTH_SHORT).show();

                    }
                }).execute(apiUrl);
            }
        });

}
private static class MakeGetRequest extends AsyncTask<String, Void, String> {

    private OnRequestListener listener;

    public MakeGetRequest(OnRequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // Get response
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            result = stringBuilder.toString();

        } catch (IOException e) {
            e.printStackTrace();
            result = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        if (result != null) {
            listener.onSuccess(result);
        } else {
            listener.onError("Failed to fetch data");
        }
    }

    public interface OnRequestListener {
        void onSuccess(String response);
        void onError(String error);
    }
}}


