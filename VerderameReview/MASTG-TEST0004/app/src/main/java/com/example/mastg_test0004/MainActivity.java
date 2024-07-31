package com.example.mastg_test0004;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private MyAPI myAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        }); Button postDataBtn= findViewById(R.id.button);
        EditText u= findViewById(R.id.user);
        EditText e= findViewById(R.id.mail);
        postDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (u.getText().toString().isEmpty() || u.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, "fill the form", Toast.LENGTH_SHORT).show();
                }
                else {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http:/10.0.2.2:5000/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    myAPI = retrofit.create(MyAPI.class);

                    // Call the method to send data
                    sendPostRequest(u.getText().toString(), e.getText().toString());
                }
            }
        });
    }
    private void sendPostRequest(String user, String email) {
        Gson gson = new GsonBuilder().create();
        MyData data = new MyData(user,email);
        String json = gson.toJson(data);
        Log.d(TAG, "sendPostRequest: "+json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), json);
        Call<Void> call = myAPI.sendData(requestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Data Sent Correctly", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, "Error Data not Sent", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Call", "Failed", t);            }
        });
    }
}
