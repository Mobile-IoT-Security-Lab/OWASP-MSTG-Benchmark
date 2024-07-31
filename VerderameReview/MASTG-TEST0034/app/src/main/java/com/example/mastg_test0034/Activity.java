package com.example.mastg_test0034;

import static android.provider.Telephony.Mms.Part.FILENAME;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Activity extends AppCompatActivity {
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        EditText u= findViewById(R.id.editTextText);
        EditText e=findViewById(R.id.mail);
        Button s= findViewById(R.id.button2);
        Button r= findViewById(R.id.button3);
        userList = new ArrayList<>();
        TextView t= findViewById(R.id.textView);
        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!u.getText().toString().isEmpty()||!e.getText().toString().isEmpty()) {
                    userList.add(new User(u.getText().toString(), e.getText().toString()));
                    Toast.makeText(Activity.this, "User appended to the List", Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(Activity.this, "Fill the Form!", Toast.LENGTH_SHORT).show();
                }
                saveUserList(Activity.this,userList);
                Toast.makeText(Activity.this, "List Correctly Saved", Toast.LENGTH_SHORT).show();
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<User> userList=retrieveUserList(Activity.this);
                t.setText("");
                for (User user : userList){
                    t.append("Name: " + user.getName() + ", Email: " + user.getEmail()+"\n");
                }

            }
        });
    }
    // Method to serialize and save  list
    private void saveUserList(Context context, List<User> userList) {
        JSONArray jsonArray = new JSONArray();
        for (User user : userList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", user.getName());
                jsonObject.put("email", user.getEmail());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                Log.e(TAG, "Error creating JSON object: " + e.getMessage());
            }
        }

        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput("credentials.json", Context.MODE_PRIVATE);
            fos.write(jsonArray.toString().getBytes());
            Log.d(TAG, "User list saved successfully");
        } catch (IOException e) {
            Log.e(TAG, "Error saving user list: " + e.getMessage());
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing file output stream: " + e.getMessage());
                }
            }
        }
    }
    private List<User> retrieveUserList(Context context) {
        List<User> userList = new ArrayList<>();

        FileInputStream fis = null;
        try {
            fis = context.openFileInput("credentials.json");
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String jsonString = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String email = jsonObject.getString("email");
                userList.add(new User(name, email));
            }

            Log.d(TAG, "User list retrieved successfully");
        } catch (IOException | JSONException e) {
            Log.e(TAG, "Error retrieving user list: " + e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing file input stream: " + e.getMessage());
                }
            }
        }

        return userList;
    }

}