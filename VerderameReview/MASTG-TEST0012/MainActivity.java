package com.example.mastg_test0012;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Timer;
import java.util.TimerTask;

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
        EditText value= findViewById(R.id.editTextTime);
        Button start= findViewById(R.id.start);
        TextView timerValue= findViewById(R.id.timerVal);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer(Integer.valueOf(value.getText().toString()),timerValue);
            }
        });
    }
    public void cancelTimer() {
        if(countDownTimer != null) countDownTimer.cancel();
    }


    private void startTimer(int value,TextView timer) {
        countDownTimer = new CountDownTimer(value*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                final int secondsRemaining = (int) (millisUntilFinished / 1000);
                timer.setText(""+secondsRemaining);
            }

            @Override
            public void onFinish() {
                timer.setText("FINISH!");
            }
        };
        countDownTimer.start();
    }


    /*
     * Fix 1: Cancel Timer when
     * activity might be completed
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}