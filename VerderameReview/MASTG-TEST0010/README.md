# [MASTG-TEST-0010: Finding Sensitive Information in Auto-Generated Screenshots](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0010)

## Overview
MASVS-PLATFORM-3 / MSTG-STORAGE-9 / May 08, 2023
## Static Analysis
A screenshot of the current activity is taken when an Android app goes into background and displayed for aesthetic purposes when the app returns to the foreground. However, this may leak sensitive information.

To determine whether the application may expose sensitive information via the app switcher, find out whether the `FLAG_SECURE` option has been set. You should find something similar to the following code snippet:

Example in Java:

```
getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);

setContentView(R.layout.activity_main);
```

Example in Kotlin:

````
window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE)

setContentView(R.layout.activity_main)
```
If the option has not been set, the application is vulnerable to screen capturing.

