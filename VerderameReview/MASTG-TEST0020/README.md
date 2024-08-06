# [MASTG-TEST-0020: Testing the TLS Settings](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0020)

## Nota

la vulnerabilità non deve essre qeusta mausare ad esempio TLS 1.0 o 1.1 si veda il link sotto


## Implementation
- create app che permette all’utente di cercare un URL e visualizzarlo all’interno della webview see l’url cercato restituisce come response code 200 .
- permesso http `android:usesCleartextTraffic="true">`
    - Abilitato internet Access (Manifest) →`<uses-permission android:name="android.permission.INTERNET" />`
    - ignora TLS issues

```java
WebView myWebView = (WebView) findViewById(R.id.webview);
myWebView.setWebViewClient(new WebViewClient(){
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //Ignore TLS certificate errors and instruct the WebViewClient to load the website
        handler.proceed();
    }
});
```

## Overview
MASVS-NETWORK-1 / MSTG-NETWORK-2 / May 08, 2023
Refer to section ["Verifying the TLS Settings"](https://mas.owasp.org/MASTG/tests/ios/MASVS-NETWORK/MASTG-TEST-0066/) in chapter "Mobile App Network Communication" for details.
