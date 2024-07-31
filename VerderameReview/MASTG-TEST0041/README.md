# [MASTG-TEST-0041: Testing for Debugging Code and Verbose Error Logging](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0041)

## Implementation

- create app che permette all’utente di cercare un URL e visualizzarlo all’interno della webview see l’url cercato restituisce come response code 200 .
- l’app ha sempre la strict mode abilitata, senza di essa quando viene fatto il check dell’url e poi mandato alla webview, l’applicazione crasherebbe ma con la strict mode abilitata funziona,in quanto la richiesta get occuperebbe per troppo tempo l’UI thread.

## Overview
MASVS-RESILIENCE-3 / MSTG-CODE-4 / May 08, 2023
## Static Analysis
To determine whether `StrictMode` is enabled, you can look for the `StrictMode.setThreadPolicy` or `StrictMode.setVmPolicy` methods. Most likely, they will be in the onCreate method.

The detection methods for the [thread policy ↗](https://javabeat.net/strictmode-android-1/) are

```
detectDiskWrites()
detectDiskReads()
detectNetwork()
```

The penalties for [thread policy violation ↗ ](https://javabeat.net/strictmode-android-1/) are

```
penaltyLog() // Logs a message to LogCat
penaltyDeath() // Crashes application, runs at the end of all enabled penalties
penaltyDialog() // Shows a dialog
```

Have a look at the [best practices ↗](https://code.tutsplus.com/android-best-practices-strictmode--mobile-7581t) for using StrictMode.