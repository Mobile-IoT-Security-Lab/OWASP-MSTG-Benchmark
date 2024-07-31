package com.example.mastg_test0038;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text= findViewById(R.id.textView);
        if (isDeviceRooted()) {
            Toast.makeText(this, "Device Rooted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Device not Rooted", Toast.LENGTH_SHORT).show();
            text.setText("Credit Card Number: 12345678\nPin Number:1234");
        }
    }
    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2() {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"};
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = in.readLine();
            if (output != null && output.contains("uid=0")) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception ignored) {}
            }
        }
    }
}