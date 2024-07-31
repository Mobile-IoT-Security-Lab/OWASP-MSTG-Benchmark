# [MASTG-TEST-0023: Testing the Security Provider](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0023)

## Implementation

Creata app (login + webview) senza questo nel gradle file:

`*implementation* ("com.google.android.gms:play-services-gcm:17.0.0")`

di conseguenza non sappiamo Se i servizi Google Play sono disponibili, dunque l’app risulta vulnerabile.

l’app fa un semplice login.(admin,1234)

## Overview
MASVS-NETWORK-1 / MSTG-NETWORK-6 / May 08, 2023
## Static Analysis
Applications based on the Android SDK should depend on GooglePlayServices. For example, in the gradle build file, you will find compile `'com.google.android.gms:play-services-gcm:x.x.x'` in the dependencies block. You need to make sure that the ProviderInstaller class is called with either `installIfNeeded` or `installIfNeededAsync`. `ProviderInstaller` needs to be called by a component of the application as early as possible. Exceptions thrown by these methods should be caught and handled correctly. If the application cannot patch its security provider, it can either inform the API of its less secure state or restrict user actions (because all HTTPS traffic should be deemed riskier in this situation).

If you have access to the source code, check if the app handle any exceptions related to the security provider updates properly, and if it reports to the backend when the application is working with an unpatched security provider. The Android Developer documentation provides different examples showing how to update the Security Provider to prevent [SSL exploits ↗](https://developer.android.com/privacy-and-security/security-gms-provider).

Lastly, make sure that NDK-based applications bind only to a recent and properly patched library that provides SSL/TLS functionality.

