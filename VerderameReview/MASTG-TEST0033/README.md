# [MASTG-TEST-0033: Testing for Java Objects Exposed Through WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0033)

## Implementation

- seguito la seguente guida: https://redfoxsec.com/blog/exploiting-android-webview-vulnerabilities/
- creato app che utilizza Javascript Bridge displayando nella webView un file `index.html` presente nella internal storage che chiama funzione `init()` che richiama:
    
    ```html
    <!-- index.html --!>
    <!DOCTYPE >
    <html xmlns="http://www.w3.org/1999/xhtml" debug="true">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
            <meta name="viewport" 
              content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
            <meta name="apple-mobile-web-app-capable" content="yes">
            <meta name="viewport" content="target-densitydpi=device-dpi" />
            <script type="text/javascript">
               function init()
               {
            	   var testVal = document.getElementById('mytextId').value;
            	   AndroidFunction.showToast(testVal);
               }
            </script>
        </head>
        <body>        
            <div style="float: left;width: 50%;">
               <input type="text" style="width: 180px;" 
                       name="myText" id="mytextId" />
               
            </div>
            <div style="clear: both;height: 3px;"> </div>
            <div>
              <input value="submit" type="button" name="submit" 
                id="btnSubmit" onclick="javascript:return init();" /> 
            </div>  
        </body>
    </html>
    
    ```
    
    ```java
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
    ```
    
    Tale funzione controlla che il valore inserito sia uguale alla parola Segreta `Secret String` 
    
- Essendoci javascript e le interfaccie abilitate e l’attività esportata l’applicazione risulta vulnerabile

## Overview 
To test for Java objects exposed through WebViews check the app for WebViews having JavaScript enabled and determine whether the WebView is creating any JavaScript interfaces aka. "JavaScript Bridges". Finally, check whether an attacker could potentially inject malicious JavaScript code.

MASVS-PLATFORM-2 / MSTG-PLATFORM-7 / May 08, 2023
## Static Analysis
The following example shows how `addJavascriptInterface` is used to bridge a Java Object and JavaScript in a WebView:

```
WebView webview = new WebView(this);
WebSettings webSettings = webview.getSettings();
webSettings.setJavaScriptEnabled(true);

MSTG_ENV_008_JS_Interface jsInterface = new MSTG_ENV_008_JS_Interface(this);

myWebView.addJavascriptInterface(jsInterface, "Android");
myWebView.loadURL("http://example.com/file.html");
setContentView(myWebView);

```
In Android 4.2 (API level 17) and above, an annotation `@JavascriptInterface` explicitly allows JavaScript to access a Java method.
```

public class MSTG_ENV_008_JS_Interface {

        Context mContext;

        /** Instantiate the interface and set the context */
        MSTG_ENV_005_JS_Interface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public String returnString () {
            return "Secret String";
        }

        /** Show a toast from the web page */
        @JavascriptInterface
        public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
        }
}
```
This is how you can call the method `returnString` from JavaScript, the string "Secret String" will be stored in the variable result:

```
var result = window.Android.returnString();
```

With access to the JavaScript code, via, for example, stored XSS or a MITM attack, an attacker can directly call the exposed Java methods.

If `addJavascriptInterface` is necessary, take the following considerations:

- Only JavaScript provided with the APK should be allowed to use the bridges, e.g. by verifying the URL on each bridged Java method (via `WebView.getUrl`).
- No JavaScript should be loaded from remote endpoints, e.g. by keeping page navigation within the app's domains and opening all other domains on the default browser (e.g. Chrome, Firefox).
- If necessary for legacy reasons (e.g. having to support older devices), at least set the minimal API level to 17 in the manifest file of the app (`<uses-sdk android:minSdkVersion="17" />`).