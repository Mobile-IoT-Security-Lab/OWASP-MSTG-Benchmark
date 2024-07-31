# [MASTG-TEST-0018: Testing Biometric Authentication](https://mas.owasp.org/MASTG/tests/android/MASVS-AUTH/MASTG-TEST-0018/)

## Implementation
- creato app che richiede autenticazione attraverso impronta digitale.
- se l’utente si autentica correttamente verrà reindirizzato ad un attività con webview.

## Overview
MASVS-AUTH-2 / MSTG-AUTH-8 / May 08, 2023
## Static Analysis
Note that there are quite some vendor/third party SDKs, which provide biometric support, but which have their own insecurities. Be very cautious when using third party SDKs to handle sensitive authentication logic.

