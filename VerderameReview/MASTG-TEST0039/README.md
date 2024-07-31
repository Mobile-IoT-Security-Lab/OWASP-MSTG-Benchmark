# [MASTG-TEST-0039: Testing whether the App is Debuggable](https://mas.owasp.org/MASTG/tests/android/MASVS-RESILIENCE/MASTG-TEST-0039)

## Implementation

- Creata app che quando viene aperta salva in un file di testo 10 username e pwd generati casualmenti
- per fare il login bisogna conoscere uno dei seguenti username e pwd
- generator apk in debug mode

## Overview
MASVS-RESILIENCE-4 / MSTG-CODE-2 / May 08, 2023
# Static Analysis
Check `AndroidManifest.xml` to determine whether the `android:debuggable` attribute has been set and to find the attribute's value:

```
    ...
    <application android:allowBackup="true" android:debuggable="true" android:icon="@drawable/ic_launcher" android:label="@string/app_name" android:theme="@style/AppTheme">
    ...
```

You can use `aapt` tool from the Android SDK with the following command line to quickly check if the `android:debuggable="true"` directive is present:

```
# If the command print 1 then the directive is present
# The regex search for this line: android:debuggable(0x0101000f)=(type 0x12)0xffffffff
$ aapt d xmltree sieve.apk AndroidManifest.xml | grep -Ec "android:debuggable\(0x[0-9a-f]+\)=\(type\s0x[0-9a-f]+\)0xffffffff"
1
```

For a release build, this attribute should always be set to `"false"` (the default value).