# [MASTG-TEST-0027: Testing for URL Loading in WebViews](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0027)

## Implementation

- creato ap che tramite login permette di visualizzare una webview vulnerabile.
- aggiunta la seguente configurazione nel manifest
    
    `<meta-data    android:name="android.webkit.WebView.EnableSafeBrowsing"    android:value="false" />`
    
- L'app consente agli utenti di fare clic sui collegamenti all'interno di WebView, che dovrebbe aprirsi nel browser Web predefinito. Tuttavia, lo sviluppatore dell'app decide di ignorare il comportamento predefinito e gestire la navigazione della pagina all'interno dell'app stessa, senza convalidare adeguatamente gli URL.
- In questo esempio, il metodo `ShouldOverrideUrlLoading` del WebViewClient personalizzato restituisce sempre true, il che significa che gestisce tutto il caricamento degli URL all'interno del WebView stesso senza un'adeguata convalida. Questo comportamento potrebbe potenzialmente consentire il caricamento di URL dannosi all'interno dell'app, causando vulnerabilità della sicurezza come attacchi di phishing o caricamento di contenuti dannosi.

## Overview
In order to test for URL loading in WebViews you need to carefully analyze handling page navigation, especially when users might be able to navigate away from a trusted environment. The default and safest behavior on Android is to let the default web browser open any link that the user might click inside the WebView. However, this default logic can be modified by configuring a WebViewClient which allows navigation requests to be handled by the app itself.

MASVS-CODE-4 / MSTG-PLATFORM-2 / May 08, 2023
## Static Analysis
To test if the app is overriding the default page navigation logic by configuring a `WebViewClient` you should search for and inspect the following interception callback functions:

- `shouldOverrideUrlLoading` allows your application to either abort loading WebViews with suspicious content by returning `true` or allow the WebView to load the URL by returning `false`. Considerations:
  - This method is not called for POST requests.
  - This method is not called for XmlHttpRequests, iFrames, "src" attributes included in HTML or `<script>` tags. Instead, `shouldInterceptRequest` should take care of this.

- `shouldInterceptRequest` allows the application to return the data from resource requests. If the return value is null, the WebView will continue to load the resource as usual. Otherwise, the data returned by the `shouldInterceptRequest` method is used. Considerations:
  - This callback is invoked for a variety of URL schemes (e.g., `http(s):`, `data:`, `file:`, etc.), not only those schemes which send requests over the network.
  - This is not called for `javascript:` or `blob:` URLs, or for assets accessed via `file:///android_asset/` or `file:///android_res/` URLs. In the case of redirects, this is only called for the initial resource URL, not any subsequent redirect URLs.
  - When Safe Browsing is enabled, these URLs still undergo Safe Browsing checks but the developer can allow the URL with `setSafeBrowsingWhitelist` or even ignore the warning via the `onSafeBrowsingHit` callback.

As you can see there are a lot of points to consider when testing the security of WebViews that have a WebViewClient configured, so be sure to carefully read and understand all of them by checking the [WebViewClient Documentation](https://developer.android.com/reference/android/webkit/WebViewClient).

## Check for EnableSafeBrowsing Disabled

While the default value of `EnableSafeBrowsing` is `true`, some applications might opt to disable it. To verify that SafeBrowsing is enabled, inspect the AndroidManifest.xml file and make sure that the configuration below is not present:

```
<manifest>
    <application>
        <meta-data android:name="android.webkit.WebView.EnableSafeBrowsing"
                   android:value="false" />
        ...
    </application>
</manifest>
```