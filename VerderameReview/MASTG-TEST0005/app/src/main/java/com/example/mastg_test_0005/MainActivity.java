package com.example.mastg_test_0005;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "1";
    private static final int NOTIFICATION_ID = 1;

    private void createNotificationChannel() {
        String string = getString(R.string.channel_name);
        String string2 = getString(R.string.channel_description);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, string, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription(string2);
        ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
    }

    public void sendNotification(String s1,String s2) {
        NotificationCompat.Builder priority = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_notification).setContentTitle("Thank your for buying a Coffe").setContentText("Credit Card Number: "+s1+"PIN: "+s2).setPriority(0);
        NotificationManagerCompat from = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, TypedValues.TYPE_TARGET);
        }
        from.notify(1, priority.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button event= findViewById(R.id.button);
        EditText text1= findViewById(R.id.editTextText);
        EditText text2= findViewById(R.id.editTextTextPassword);
        createNotificationChannel();
        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String credit= text1.getText().toString();
                String pin= text2.getText().toString();
                if (credit.equals("")|| pin.equals("")){
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(MainActivity.this, "Fill the form", duration).show();

                }
                MainActivity.this.sendNotification(credit,pin);
            }
        });
    }
}