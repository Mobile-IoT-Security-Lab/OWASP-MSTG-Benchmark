# [MASTG-TEST-0021: Testing Endpoint Identify Verification](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0021)
## Implementation
**Verifying the Target SDK Version**

- creato app che visualizza in webview `https://www.example.com` con

  `targetSdkVersion: '23'` 

- per ragioni di sicurezza `targetSdkVersion` in apktool.yml deve essere uguale o superiore a `24`.
- Tuttavia, anche se `targetSdkVersion >=24`, lo sviluppatore può disabilitare le protezioni predefinite utilizzando una configurazione di sicurezza di rete personalizzata definendo un trust Anchor personalizzato **costringendo l'app a fidarsi delle CA fornite dall'utente**.
- la guida dice di andare a controller le network conf e ispezionare qualsiasi `<trust-anchors>` personalizzato che definisca `<certificates src="user">` (che dovrebbe essere evitato).
- ma creando il file risulta vulnerable in quanto lascio l’accesso a tutti perchè ho target SDK 23
- non viene eseguita analisi dei certificati tramite `TrustManager`
- ignora TLS issues:
    
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
    
- disabilitato HostName Verifier
    
    ```java
            final HostnameVerifier NO_VERIFY = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
    ```
## Overview
MASVS-NETWORK-1 / MSTG-NETWORK-3 / September 29, 2023
## Static Analysis

Using TLS to transport sensitive information over the network is essential for security. However, encrypting communication between a mobile application and its backend API is not trivial. Developers often decide on simpler but less secure solutions (e.g., those that accept any certificate) to facilitate the development process, and sometimes these weak solutions make it into the production version , potentially exposing users to man-in-the-middle attacks .

Two key issues should be addressed:

- Verify that a certificate comes from a trusted source, i.e. a trusted CA (Certificate Authority).
- Determine whether the endpoint server presents the right certificate.

Make sure that the hostname and the certificate itself are verified correctly. Examples and common pitfalls are available in the official [Android documentation](https://developer.android.com/privacy-and-security/security-ssl). Search the code for examples of `TrustManager` and `HostnameVerifier` usage. In the sections below, you can find examples of the kind of insecure usage that you should look for.

Note that from Android 8.0 (API level 26) onward, there is no support for SSLv3 and HttpsURLConnection will no longer perform a fallback to an insecure TLS/SSL protocol.

## Verifying the Target SDK Version¶
Applications targeting Android 7.0 (API level 24) or higher will use a default Network Security Configuration that doesn't trust any user supplied CAs, reducing the possibility of MITM attacks by luring users to install malicious CAs.

Decode the app using apktool and verify that the `targetSdkVersion` in apktool.yml is equal to or higher than `24`.

```
grep targetSdkVersion UnCrackable-Level3/apktool.yml
  targetSdkVersion: '28'
```
However, even if `targetSdkVersion >=24`, the developer can disable default protections by using a custom Network Security Configuration defining a custom trust anchor forcing the app to trust user supplied CAs. See "[Analyzing Custom Trust Anchors](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0021/#analyzing-custom-trust-anchors)".

## Analyzing Custom Trust Anchors
Search for the Network Security Configuration file and inspect any custom <trust-anchors> defining <certificates src="user"> (which should be avoided).

You should carefully analyze the precedence of entries ↗:

- If a value is not set in a <domain-config> entry or in a parent <domain-config>, the configurations in place will be based on the <base-config>
- If not defined in this entry, the default configurations will be used.

Take a look at this example of a Network Security Configuration for an app targeting Android 9 (API level 28):

```
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config>
        <domain includeSubdomains="false">owasp.org</domain>
        <trust-anchors>
            <certificates src="system" />
            <certificates src="user" />
        </trust-anchors>
    </domain-config>
</network-security-config>
```
Some observations:

- There's no `<base-config>`, meaning that the default configuration for Android 9 (API level 28) or higher will be used for all other connections (only system CA will be trusted in principle).
- However, the `<domain-config>` overrides the default configuration allowing the app to trust both system and user CAs for the indicated `<domain>` (owasp.org).
- This doesn't affect subdomains because of includeSubdomains="false".

Putting all together we can translate the above Network Security Configuration to: "the app trusts system and user CAs for the owasp.org domain, excluding its subdomains. For any other domains the app will trust the system CAs only".

## Verifying the Server Certificate

`TrustManager` is a means of verifying conditions necessary for establishing a trusted connection in Android. The following conditions should be checked at this point:

- Has the certificate been signed by a trusted CA?
- Has the certificate expired?
- Is the certificate self-signed?

The following code snippet is sometimes used during development and will accept any certificate, overwriting the functions `checkClientTrusted, checkServerTrusted, and getAcceptedIssuers`. Such implementations should be avoided, and, if they are necessary, they should be clearly separated from production builds to avoid built-in security flaws.

```
TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        }
    }
 };

// SSLContext context
context.init(null, trustAllCerts, new SecureRandom());
```
## WebView Server Certificate Verification

Sometimes applications use a WebView to render the website associated with the application. This is true of HTML/JavaScript-based frameworks such as Apache Cordova, which uses an internal WebView for application interaction. When a WebView is used, the mobile browser performs the server certificate validation. Ignoring any TLS error that occurs when the WebView tries to connect to the remote website is a bad practice.

The following code will ignore TLS issues, exactly like the WebViewClient custom implementation provided to the WebView:

```
WebView myWebView = (WebView) findViewById(R.id.webview);
myWebView.setWebViewClient(new WebViewClient(){
    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //Ignore TLS certificate errors and instruct the WebViewClient to load the website
        handler.proceed();
    }
});
```

## Apache Cordova Certificate Verification¶
Implementation of the Apache Cordova framework's internal WebView usage will ignore TLS errors  in the method `onReceivedSslError` if the flag `android:debuggable` is enabled in the application manifest. Therefore, make sure that the app is not debuggable. See the test case "Testing If the App is Debuggable".

## Hostname Verification
Another security flaw in client-side TLS implementations is the lack of hostname verification. Development environments usually use internal addresses instead of valid domain names, so developers often disable hostname verification (or force an application to allow any hostname) and simply forget to change it when their application goes to production. The following code disables hostname verification:

```
final static HostnameVerifier NO_VERIFY = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};
``
With a built-in HostnameVerifier, accepting any hostname is possible:

```
HostnameVerifier NO_VERIFY = org.apache.http.conn.ssl.SSLSocketFactory
                             .ALLOW_ALL_HOSTNAME_VERIFIER;
Make sure that your application verifies a hostname before setting a trusted connection.


```
