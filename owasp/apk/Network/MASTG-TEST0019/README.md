# MASTG-TEST-0019: Testing Data Encryption on the Network
## Overview
## Static Analysis
## Testing Network Requests over Secure Protocols
First, you should identify all network requests in the source code and ensure that no plain HTTP URLs are used. Make sure that sensitive information is sent over secure channels by using [`HttpsURLConnection ↗`](https://developer.android.com/reference/javax/net/ssl/HttpsURLConnection.html) or [`SSLSocket ↗`](https://developer.android.com/reference/javax/net/ssl/SSLSocket.html) (for socket-level communication using TLS).

## Testing Network API Usage

Next, even when using a low-level API which is supposed to make secure connections (such as SSLSocket), be aware that it has to be securely implemented. For instance,` SSLSocket ` doesn't verify the hostname. Use `getDefaultHostnameVerifier` to verify the hostname. The Android developer documentation includes a [code example ↗](https://developer.android.com/privacy-and-security/security-ssl#WarningsSslSocket).

## Testing for Cleartext Traffic

Next, you should ensure that the app is not allowing cleartext HTTP traffic. Since Android 9 (API level 28) cleartext HTTP traffic is blocked by default (thanks to the default Network Security Configuration) but there are multiple ways in which an application can still send it:

- Setting the `android:usesCleartextTraffic` attribute of the `<application>` tag in the AndroidManifest.xml file. Note that this flag is ignored in case the Network Security Configuration is configured.
- Configuring the Network Security Configuration to enable cleartext traffic by setting the `cleartextTrafficPermitted` attribute to true on `<domain-config>` elements.
- Using low-level APIs (e.g. Socket ↗) to set up a custom HTTP connection.
- Using a cross-platform framework (e.g. Flutter, Xamarin, ...), as these typically have their own implementations for HTTP libraries.

All of the above cases must be carefully analyzed as a whole. For example, even if the app does not permit cleartext traffic in its Android Manifest or Network Security Configuration, it might actually still be sending HTTP traffic. That could be the case if it's using a low-level API (for which Network Security Configuration is ignored) or a badly configured cross-platform framework.

For more information refer to the article ["Security with HTTPS and SSL" ↗](https://developer.android.com/privacy-and-security/security-ssl).

