package com.example.mastg_test0051;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String PROMO_CODE="TALOS24";
    private static final String CHANNEL_ID = "1";
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button promo= findViewById(R.id.promo);
        Button buy= findViewById(R.id.buy);
        EditText text= findViewById(R.id.editTextText);
        createNotificationChannel();
        promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.equals("") || text.equals(PROMO_CODE)){
                    Toast.makeText(MainActivity.this, "Insert Valid Promo Code!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, "Congratulations! You got this product for free", Toast.LENGTH_SHORT).show();
                    buy.setText("Buy Coffe for free!");
                }
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buy.getText().toString().equals("Buy Coffe for free!")){
                    MainActivity.this.sendNotification("Thank you, you by a coffe!");
                }
                else {
                    Toast.makeText(MainActivity.this, "Not Enough Credit", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void createNotificationChannel() {
        String string = getString(R.string.channel_name);
        String string2 = getString(R.string.channel_description);
        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, string, NotificationManager.IMPORTANCE_DEFAULT);
        notificationChannel.setDescription("SHOP");
        ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
    }

    public void sendNotification(String s1) {
        NotificationCompat.Builder priority = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.ic_notification).setContentTitle("Thank your for buying a Coffe").setContentText("You Paid 0$").setPriority(0);
        NotificationManagerCompat from = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, "android.permission.POST_NOTIFICATIONS") != 0) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.POST_NOTIFICATIONS"}, TypedValues.TYPE_TARGET);
        }
        from.notify(1, priority.build());
    }

}