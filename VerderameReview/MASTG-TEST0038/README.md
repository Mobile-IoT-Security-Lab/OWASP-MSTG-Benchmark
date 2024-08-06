# [MASTG-TEST-0038: Making Sure that the App is Properly Signed](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0038)

##
Non capisco vulnerabilità implementata


## Implementation

- Creata app che controlla se il dispositive è rootato o no
- se il dispositive è rootato non ti fa vedere nulla
- se il dispositive non è rootato viene mostrato un segreto nascosto

## Overview
MASVS-RESILIENCE-2 / MSTG-CODE-1 / May 08, 2023
## Static Analysis
Make sure that the release build has been signed via both the v1 and v2 schemes for Android 7.0 (API level 24) and above and via all the three schemes for Android 9 (API level 28) and above, and that the code-signing certificate in the APK belongs to the developer.

APK signatures can be verified with the `apksigner` tool. It is located at `[SDK-Path]/build-tools/[version]`.

```
$ apksigner verify --verbose Desktop/example.apk
Verifies
Verified using v1 scheme (JAR signing): true
Verified using v2 scheme (APK Signature Scheme v2): true
Verified using v3 scheme (APK Signature Scheme v3): true
Number of signers: 1
```
The contents of the signing certificate can be examined with `jarsigner`. Note that the Common Name (CN) attribute is set to "Android Debug" in the debug certificate.

The output for an APK signed with a debug certificate is shown below:

```
$ jarsigner -verify -verbose -certs example.apk

sm     11116 Fri Nov 11 12:07:48 ICT 2016 AndroidManifest.xml

      X.509, CN=Android Debug, O=Android, C=US
      [certificate is valid from 3/24/16 9:18 AM to 8/10/43 9:18 AM]
      [CertPath not validated: Path doesn\'t chain with any of the trust anchors]
(...)
```

Ignore the "CertPath not validated" error. This error occurs with Java SDK 7 and above. Instead of `jarsigner`, you can rely on the apksigner to verify the certificate chain.

The signing configuration can be managed through Android Studio or the signingConfig block in build.gradle. To activate both the v1 and v2 schemes, the following values must be set:

```
v1SigningEnabled true
v2SigningEnabled true
```
Several best practices for [configuring the app for release ↗](https://developer.android.com/studio/publish/preparing?hl=it#publishing-configure) are available in the official Android developer documentation.

Last but not least: make sure that the application is never deployed with your internal testing certificates.

