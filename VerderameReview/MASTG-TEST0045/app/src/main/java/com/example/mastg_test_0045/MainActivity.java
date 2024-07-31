package com.example.mastg_test_0045;

import static java.lang.System.getProperty;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView Secret= findViewById(R.id.scrt);
        Secret.setText("Talos sec");
    }
}