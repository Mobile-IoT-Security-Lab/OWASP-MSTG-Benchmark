package com.example.mastg_test0030;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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
        Button b= findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent base = new Intent();
                @SuppressLint("MutableImplicitPendingIntent") PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, base, FLAG_MUTABLE);
                Intent notificationIntent= new Intent();
                notificationIntent.putExtra("key", pendingIntent);
                notificationIntent.setAction("VIEW_CONTACTS");
                String string = getString(R.string.channel_name);
                String string2 = getString(R.string.channel_description);
                NotificationChannel notificationChannel = new NotificationChannel("1", string, NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription(string2);
                ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "1")
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("FREE COFFE")
                        .setContentText("Click to view details")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat from = NotificationManagerCompat.from(MainActivity.this);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, "android.permission.POST_NOTIFICATIONS") != 0) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.POST_NOTIFICATIONS"}, TypedValues.TYPE_TARGET);
                }
                sendBroadcast(notificationIntent);
                from.notify(1, builder.build());

            }

        });

    }
}
