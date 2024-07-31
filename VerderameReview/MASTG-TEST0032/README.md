# [MASTG-TEST-0032: Testing WebView Protocol Handlers](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0032)

## Implementation

- aggiunte network config file:
    
    ```xml
    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <domain-config cleartextTrafficPermitted="true">
            <domain includeSubdomains="true">10.0.2.2</domain>
        </domain-config>
    </network-security-config>
    ```
    
    la seguente app fa una load sulla web view di un contenuto salvato sull’external storage `home.html` :
    
    ```xml
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login</title>
    </head>
    <body>
        <p>For the login on Poste follow this <a href="https://securelogin.poste.it/jod-fcc/fcc-authentication.html">link</a></p>
    
    </body>
    </html>
    ```
    
    rendendo quindi a chiunque la possibilità di sovrascrivere quel file


## Overview
To test for WebView protocol handlers check the app for WebView usage and evaluate whether or not the WebView should have resource access. If resource access is necessary you need to verify that it's implemented following best practices.

## Static Analysis
Check the source code for WebView usage. The following [WebView settings ↗](https://developer.android.com/reference/android/webkit/WebSettings) control resource access:

- `setAllowContentAccess`: Content URL access allows WebViews to load content from a content provider installed on the system, which is enabled by default .
- `setAllowFileAccess`: Enables and disables file access within a WebView. The default value is true when targeting Android 10 (API level 29) and below and false for Android 11 (API level 30) and above. Note that this enables and disables file system access ↗ only. Asset and resource access is unaffected and accessible via file:///android_asset and file:///android_res.
- `setAllowFileAccessFromFileURLs`: Does or does not allow JavaScript running in the context of a file scheme URL to access content from other file scheme URLs. The default value is true for Android 4.0.3 - 4.0.4 (API level 15) and below and false for Android 4.1 (API level 16) and above.
- `setAllowUniversalAccessFromFileURLs`: Does or does not allow JavaScript running in the context of a file scheme URL to access content from any origin. The default value is true for Android 4.0.3 - 4.0.4 (API level 15) and below and false for Android 4.1 (API level 16) and above.

If one or more of the above methods is/are activated, you should determine whether the method(s) is/are really necessary for the app to work properly.

If a WebView instance can be identified, find out whether local files are loaded with the loadURL ↗ method.

```
WebView = new WebView(this);
webView.loadUrl("file:///android_asset/filename.html");
```

The location from which the HTML file is loaded must be verified. If the file is loaded from external storage, for example, the file is readable and writable by everyone. This is considered a bad practice. Instead, the file should be placed in the app's assets directory.

```
webview.loadUrl("file:///" +
Environment.getExternalStorageDirectory().getPath() +
"filename.html");
```

The URL specified in `loadURL` should be checked for dynamic parameters that can be manipulated; their manipulation may lead to local file inclusion.

Use the following code snippet and best practices ↗ to deactivate protocol handlers, if applicable:


//If attackers can inject script into a WebView, they could access local resources. This can be prevented by disabling local file system access, which is enabled by default. You can use the Android WebSettings class to disable local file system access via the public method `setAllowFileAccess`.
```
webView.getSettings().setAllowFileAccess(false);

webView.getSettings().setAllowFileAccessFromFileURLs(false);

webView.getSettings().setAllowUniversalAccessFromFileURLs(false);

webView.getSettings().setAllowContentAccess(false);
```

- Create a list that defines local and remote web pages and protocols that are allowed to be loaded.
- Create checksums of the local HTML/JavaScript files and check them while the app is starting up. Minify JavaScript files to make them harder to read.    