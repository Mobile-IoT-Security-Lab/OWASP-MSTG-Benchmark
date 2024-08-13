# [MASTG-TEST-0014: Testing the Configuration of Cryptographic Standard Algorithms](https://mas.owasp.org/MASTG/tests/android/MASVS-CRYPTO/MASTG-TEST-0014)
## Implementation 
- creato login ,le credenziali sono salvate ,(Admin,1234), all’interno delle shared preferences
- usare una chiave per le sharedpref
- usare la stessa chiave per creare un altro file shared pref e/o per inviare dati via internet.

```bash
//DEFAULT
MessageDigest md = MessageDigest.getInstance("MD5");

//Custom
MessageDigest md = MessageDigest.getInstance("MD5","BC");

```
## Overview
MASVS-CRYPTO-1 / MSTG-CRYPTO-2 / MSTG-CRYPTO-3 / MSTG-CRYPTO-4 / May 13, 2024
## Static Analysis
Identify all the instances of the cryptographic primitives in code. Identify all custom cryptography implementations. You can look for:

- classes `Cipher`, `Mac`, `MessageDigest`, `Signature`
- interfaces `Key`, `PrivateKey`, `PublicKey`, `SecretKey`
- functions `getInstance`, `generateKey`
- exceptions `KeyStoreException`, `CertificateException`, `NoSuchAlgorithmException`
- classes which use `java.security.*`, `javax.crypto.*`, `android.security.*` and `android.security.keystore.*` packages.

Identify that all calls to `getInstance` use default `provider` of security services by not specifying it (it means AndroidOpenSSL aka Conscrypt). `Provider` can only be specified in `KeyStore` related code (in that situation `KeyStore` should be provided as `provider`). If other `provider` is specified it should be verified according to situation and business case (i.e. Android API version), and `provider` should be examined against potential vulnerabilities.

Ensure that the best practices outlined in the ["Cryptography for Mobile Apps"](https://github.com/google/android-emulator-container-scripts) chapter are followed. Look at insecure and deprecated algorithms and [common configuration issues](https://github.com/google/android-emulator-container-scripts).
