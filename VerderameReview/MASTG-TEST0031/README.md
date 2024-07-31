# [MASTG-TEST-0031: Testing JavaScript Execution in WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0031)

## Implementation

- Creata app che permette di scrivere in un editText cio che vorremo cercare su internet e il risultato viene poi loadato sulla web view (`https://…`):
    - Abilitato internet Access (Manifest) →`<uses-permission android:name="android.permission.INTERNET" />`
    - Abilitato js → `view.getSettings().setJavaScriptEnabled(true);`

## Overview

To test for JavaScript execution in WebViews check the app for WebView usage and evaluate whether or not each WebView should allow JavaScript execution. If JavaScript execution is required for the app to function normally, then you need to ensure that the app follows the all best practices.
MASVS-PLATFORM-2 / MSTG-PLATFORM-5 / May 08, 20

## Static Analysis

To create and use a WebView, an app must create an instance of the `WebView` class.

```
WebView webview = new WebView(this);
setContentView(webview);
webview.loadUrl("https://www.owasp.org/");
```
Various settings can be applied to the WebView (activating/deactivating JavaScript is one example). JavaScript is disabled by default for WebViews and must be explicitly enabled. Look for the method setJavaScriptEnabled to check for JavaScript activation.
javaCopywebview.getSettings().setJavaScriptEnabled(true);
This allows the WebView to interpret JavaScript. It should be enabled only if necessary to reduce the attack surface to the app. If JavaScript is necessary, you should make sure that:

- The communication to the endpoints consistently relies on HTTPS (or other protocols that allow encryption) to protect HTML and JavaScript from tampering during transmission.
- JavaScript and HTML are loaded locally, from within the app data directory or from trusted web servers only.
- The user cannot define which sources to load by means of loading different resources based on a user provided input.

To remove all JavaScript source code and locally stored data, clear the WebView's cache with `clearCache` when the app closes.

Devices running platforms older than Android 4.4 (API level 19) use a version of WebKit that has several security issues. As a workaround, the app must confirm that WebView objects display only trusted content if the app runs on these devices.
