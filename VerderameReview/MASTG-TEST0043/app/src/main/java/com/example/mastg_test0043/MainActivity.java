package com.example.mastg_test0043;

import android.os.Bundle;
import android.os.CountDownTimer;
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

import java.sql.Time;

public class MainActivity extends AppCompatActivity {
    private CountDownTimer countDownTimer;

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
        Button Start= findViewById(R.id.button);
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
    } /*
     * Mistake 1: Cancel Timer is never called
     * even though activity might be completed
     * */
    public void cancelTimer() {
        if(countDownTimer != null) countDownTimer.cancel();
    }


    private void startTimer() {
        EditText TimerValue = findViewById(R.id.editTextNumber);
        if (!TimerValue.getText().toString().isEmpty()) {
            int value = Integer.parseInt(TimerValue.getText().toString());

            countDownTimer = new CountDownTimer(value * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    TextView timer = findViewById(R.id.textView);

                    final int secondsRemaining = (int) (millisUntilFinished / 1000);
                    timer.setText(String.valueOf(secondsRemaining));
                }

                @Override
                public void onFinish() {
                    TextView timer = findViewById(R.id.textView);

                    timer.setText("STOP");
                }
            };
            countDownTimer.start();
        }
        else{
            Toast.makeText(MainActivity.this, "Fill the form", Toast.LENGTH_SHORT).show();

        }
    }

}