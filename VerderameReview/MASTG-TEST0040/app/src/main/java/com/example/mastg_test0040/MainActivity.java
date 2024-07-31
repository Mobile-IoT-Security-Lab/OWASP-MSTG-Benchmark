package com.example.mastg_test0040;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
public class MainActivity extends AppCompatActivity {
        static {
            System.loadLibrary("mastg_test0040");
        }

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
            EditText val1= findViewById(R.id.editTextNumber);
            EditText val2 = findViewById(R.id.editTextNumber2);
            Button eq= findViewById(R.id.button);
            TextView r= findViewById(R.id.res);
            eq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (val1.getText().toString().trim().isEmpty() || val2.getText().toString().trim().isEmpty()){
                        Toast.makeText(MainActivity.this, "Fill The Form", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        r.setText(""+sumFloats(Float.valueOf(val1.getText().toString()),Float.valueOf(val2.getText().toString())));
                    }
                }
            });
        }
        public native float sumFloats(float a, float b);

    }