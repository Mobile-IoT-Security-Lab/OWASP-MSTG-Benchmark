# [MASTG-TEST-0012: Testing the Device-Access-Security Policy](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0012)

## Implementation
- Creato Timer con API level ( Android 5.1)
- Per testare la policy di sicurezza di accesso al dispositivo applicata dall'app, è necessario fornire una copia scritta della policy. La politica dovrebbe definire i controlli disponibili e la loro applicazione. Ad esempio, un controllo potrebbe richiedere che l'app funzioni solo su Android 6.0 (livello API 23) o una versione più recente, chiudendo l'app o visualizzando un avviso se la versione di Android è inferiore alla 6.0.

## Overview

Apps that process or query sensitive information should run in a trusted and secure environment. To create this environment, the app can check the device for the following:

- PIN- or password-protected device locking
- Recent Android OS version
- USB Debugging activation
- Device encryption
- Device rooting (see also "Testing Root Detection")
MASVS-STORAGE-1 / MSTG-STORAGE-11 / May 08, 2023
## Static Analysis

To test the device-access-security policy that the app enforces, a written copy of the policy must be provided. The policy should define available checks and their enforcement. For example, one check could require that the app run only on Android 6.0 (API level 23) or a more recent version, closing the app or displaying a warning if the Android version is less than 6.0.

Check the source code for functions that implement the policy and determine whether it can be bypassed.

You can implement checks on the Android device by querying [Settings.Secure](https://developer.android.com/reference/android/provider/Settings.Secure) for system preferences. [Device Administration API](https://developer.android.com/guide/topics/admin/device-admin) offers techniques for creating applications that can enforce password policies and device encryption.
