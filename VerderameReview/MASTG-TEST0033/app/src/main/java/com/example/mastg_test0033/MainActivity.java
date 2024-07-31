package com.example.mastg_test0033;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileOutputStream;
import java.io.IOException;

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
        CreateFile();
        Button l= findViewById(R.id.button);
        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SupportedWebView.class);
                intent.putExtra("url","/data/data/com.example.mastg_test0033/files/index.html");
                startActivity(intent);
            }
        });
    }
    public void CreateFile(){
        String fileName = "index.html"; // Name of the file to create
        String fileContents = "<!DOCTYPE >\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" debug=\"true\">\n" +
                "    <head>\n" +
                "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n" +
                "        <meta name=\"viewport\" \n" +
                "          content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0\">\n" +
                "        <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "        <meta name=\"viewport\" content=\"target-densitydpi=device-dpi\" />\n" +
                "        <script type=\"text/javascript\">\n" +
                "           function init()\n" +
                "           {\n" +
                "        \t   var testVal = document.getElementById('mytextId').value;\n" +
                "        \t   AndroidFunction.showToast(testVal);\n" +
                "           }\n" +
                "        </script>\n" +
                "    </head>\n" +
                "    <body>        \n" +
                "        <div style=\"float: left;width: 50%;\">\n" +
                "           <input type=\"text\" style=\"width: 180px;\" \n" +
                "                   name=\"myText\" id=\"mytextId\" />\n" +
                "           \n" +
                "        </div>\n" +
                "        <div style=\"clear: both;height: 3px;\"> </div>\n" +
                "        <div>\n" +
                "          <input value=\"submit\" type=\"button\" name=\"submit\" \n" +
                "            id=\"btnSubmit\" onclick=\"javascript:return init();\" /> \n" +
                "        </div>  \n" +
                "    </body>\n" +
                "</html>\n";

        try {
            Context context = this; // Replace getContext() with your app's context retrieval

            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.close();
            System.out.println("File created: " + fileName);
        } catch (IOException e) {
            System.out.println("Error occurred while creating the file: " + e.getMessage());
        }
    }
}