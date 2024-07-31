package com.example.mastg_test0033;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class MyJavaScriptInterface {

    Context mContext;
    String Secret="Secret String";
    public MyJavaScriptInterface(Context c) {
        mContext = c;
    }
    @JavascriptInterface
    public String returnString () {
        return Secret;
    }
    @JavascriptInterface
    public void showToast(String val) {
        String webMessage = val;
        if( val.equals(returnString())){
            Toast.makeText(mContext, "Correct!", Toast.LENGTH_SHORT).show();

        }
        else{
            Toast.makeText(mContext, "Wrong Secret", Toast.LENGTH_SHORT).show();

        }
    }
}