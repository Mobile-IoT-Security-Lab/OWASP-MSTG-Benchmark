# [MASTG-TEST-0028: Testing Deep Links](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0028)

## Nota

mobfs28 è vuoto
P perchè trova deeplink ma non può essere validato e sanitizzato

## Implementation 

- creata app con `minSdkVersion` uguale a 26
- nella seguente app abbiamo un login(admin,1234)che ti permette di accedere ad una webView
- la vunerabilità nella seguente app è che si puo bypassare il login generando un intent da terminale e si puo modificare anche quello che puo essere visualizzato nella webView
- seguito principalmente la seguente guida: https://z4ki.medium.com/android-deep-links-exploitation-4abade4d45b4

## Overview
Any existing deep links (including App Links) can potentially increase the app attack surface. This includes many risks ↗ such as link hijacking, sensitive functionality exposure, etc.

- Before Android 12 (API level 31), if the app has any non-verifiable links , it can cause the system to not verify all Android App Links for that app.
- Starting on Android 12 (API level 31), apps benefit from a [reduced attack surface ↗](https://developer.android.com/training/app-links/deep-linking?hl=it). A generic web intent resolves to the user's default browser app unless the target app is approved for the specific domain contained in that web intent.

All deep links must be enumerated and verified for correct website association. The actions they perform must be well tested, especially all input data, which should be deemed untrustworthy and thus should always be validated.

None of the input from these sources can be trusted; it must be validated and/or sanitized. Validation ensures processing of data that the app is expecting only. If validation is not enforced, any input can be sent to the app, which may allow an attacker or malicious app to exploit app functionality.

MASVS-PLATFORM-1 / MSTG-PLATFORM-3 / May 13, 2024
## Static Analysis
## Check for ANdroid OS Version
The Android version in which the app runs also influences the risk of using deep links. Inspect the Android Manifest to check if minSdkVersion is 31 or higher.

- Before Android 12 (API level 31), if the app has any non-verifiable deep links ↗, it can cause the system to not verify all Android App Links for that app.

- Starting on Android 12 (API level 31), apps benefit from a reduced attack surface ↗. A generic web intent resolves to the user's default browser app unless the target app is approved for the specific domain contained in that web intent.
## Check for Deep Link Usage
Inspecting the Android Manifest:

You can easily determine whether deep links (with or without custom URL schemes) are defined by decoding the app using apktool and inspecting the Android Manifest file looking for `<intent-filter>` [elements ↗](https://developer.android.com/guide/components/intents-filters?hl=it#DataTest).

Custom Url Schemes: The following example specifies a deep link with a custom URL scheme called myapp://.
```
<activity android:name=".MyUriActivity">
  <intent-filter>
      <action android:name="android.intent.action.VIEW" />
      <category android:name="android.intent.category.DEFAULT" />
      <category android:name="android.intent.category.BROWSABLE" />
      <data android:scheme="myapp" android:host="path" />
  </intent-filter>
</activity>
```
Deep Links: The following example specifies a deep Link using both the http:// and https:// schemes, along with the host and path that will activate it (in this case, the full URL would be https://www.myapp.com/my/app/path):
```
<intent-filter>
  ...
  <data android:scheme="http" android:host="www.myapp.com" android:path="/my/app/path" />
  <data android:scheme="https" android:host="www.myapp.com" android:path="/my/app/path" />
</intent-filter>
```
App Links: If the `<intent-filter>` includes the flag `android:autoVerify="true"`, this causes the Android system to reach out to the declared `android:host` in an attempt to access the [Digital Asset Links file](https://developers.google.com/digital-asset-links/v1/getting-started?hl=it) ↗ in order to verify the App Links . A deep link can be considered an App Link only if the verification is successful.

```
<intent-filter android:autoVerify="true">
```
When listing deep links remember that `<data>` elements within the same `<intent-filter>` are actually merged together to account for all variations of their combined attributes.

```
<intent-filter>
  ...
  <data android:scheme="https" android:host="www.example.com" />
  <data android:scheme="app" android:host="open.my.app" />
</intent-filter>
```
It might seem as though this supports only https://www.example.com and `app://open.my.app`. However, it actually supports:

- `https://www.example.com`
- `app://open.my.app`
- `app://www.example.com`
- `https://open.my.app`

Using Dumpsys:

Use adb to run the following command that will show all schemes:

```
adb shell dumpsys package com.example.package
```

Using Android "App Link Verification" Tester:

Use the Android["App Link Verification"](https://github.com/inesmartins/Android-App-Link-Verification-Tester) ↗ script to list all deep links (list-all) or only app links (list-applinks):

```
python3 deeplink_analyser.py -op list-all -apk ~/Downloads/example.apk

.MainActivity

app://open.my.app
app://www.example.com
https://open.my.app
https://www.example.com
```
## Check for Correct Website Association
Even if deep links contain the android:autoVerify="true" attribute, they must be actually verified in order to be considered App Links. You should test for any possible misconfigurations that might prevent full verification.

## Automatic Verification
Use the Android "App Link Verification" Tester ↗ script to get the verification status for all app links (verify-applinks). See an example here ↗.

Only on Android 12 (API level 31) or higher:

You can use adb to test the verification logic regardless of whether the app targets Android 12 (API level 31) or not. This feature allows you to:

invoke the verification process manually ↗.
reset the state of the target app's Android App Links on your device ↗.
invoke the domain verification process ↗.
You can also review the verification results ↗. For example:

```
adb shell pm get-app-links com.example.package

com.example.package:
    ID: 01234567-89ab-cdef-0123-456789abcdef
    Signatures: [***]
    Domain verification state:
      example.com: verified
      sub.example.com: legacy_failure
      example.net: verified
      example.org: 1026
```

The same information can be found by running adb shell dumpsys package com.example.package (only on Android 12 (API level 31) or higher).

## Manual Verification
This section details a few, of potentially many, reasons why the verification process failed or was not actually triggered. See more information in the Android Developers Documentation ↗ and in the white paper "Measuring the Insecurity of Mobile Deep Links of Android" ↗.

Check the Digital Asset Links file ↗:

Check for missing Digital Asset Links file:
try to find it in the domain's /.well-known/ path. Example: https://www.example.com/.well-known/assetlinks.json
or try https://digitalassetlinks.googleapis.com/v1/statements:list?source.web.site=www.example.com
Check for valid Digital Asset Links file served via HTTP.
Check for invalid Digital Asset Links files served via HTTPS. For example:
the file contains invalid JSON.
the file doesn't include the target app's package.
Check for Redirects:

To enhance the app security, the system doesn't verify any Android App Links ↗ for an app if the server sets a redirect such as http://example.com to https://example.com or example.com to www.example.com.

Check for Subdomains:

If an intent filter lists multiple hosts with different subdomains, there must be a valid Digital Asset Links file on each domain. For example, the following intent filter includes www.example.com and mobile.example.com as accepted intent URL hosts.

```
<application>
  <activity android:name=”MainActivity”>
    <intent-filter android:autoVerify="true">
      <action android:name="android.intent.action.VIEW" />
      <category android:name="android.intent.category.DEFAULT" />
      <category android:name="android.intent.category.BROWSABLE" />
      <data android:scheme="https" />
      <data android:scheme="https" />
      <data android:host="www.example.com" />
      <data android:host="mobile.example.com" />
    </intent-filter>
  </activity>
</application>
```
In order for the deep links to correctly register, a valid Digital Asset Links file must be published at both https://www.example.com/.well-known/assetlinks.json and https://mobile.example.com/.well-known/assetlinks.json.

Check for Wildcards:

If the hostname includes a wildcard (such as *.example.com), you should be able to find a valid Digital Asset Links file at the root hostname: https://example.com/.well-known/assetlinks.json.

## Check the Handler Method
Even if the deep link is correctly verified, the logic of the handler method should be carefully analyzed. Pay special attention to deep links being used to transmit data (which is controlled externally by the user or any other app).

First, obtain the name of the Activity from the Android Manifest `<activity>` element which defines the target `<intent-filter>` and search for usage of getIntent ↗ and getData ↗. This general approach of locating these methods can be used across most applications when performing reverse engineering and is key when trying to understand how the application uses deep links and handles any externally provided input data and if it could be subject to any kind of abuse.

The following example is a snippet from an exemplary Kotlin app decompiled with jadx. From the static analysis we know that it supports the deep link `deeplinkdemo://load.html/` as part of com.`mstg.deeplinkdemo.WebViewActivity`.

```
// snippet edited for simplicity
public final class WebViewActivity extends AppCompatActivity {
    private ActivityWebViewBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        Uri data = getIntent().getData();
        String html = data == null ? null : data.getQueryParameter("html");
        Uri data2 = getIntent().getData();
        String deeplink_url = data2 == null ? null : data2.getQueryParameter("url");
        View findViewById = findViewById(R.id.webView);
        if (findViewById != null) {
            WebView wv = (WebView) findViewById;
            wv.getSettings().setJavaScriptEnabled(true);
            if (deeplink_url != null) {
                wv.loadUrl(deeplink_url);
            ...
```
You can simply follow the deeplink_url String variable and see the result from the wv.loadUrl call. This means the attacker has full control of the URL being loaded to the WebView (as shown above has JavaScript enabled.

The same WebView might be also rendering an attacker controlled parameter. In that case, the following deep link payload would trigger Reflected Cross-Site Scripting (XSS) within the context of the WebView:

```
deeplinkdemo://load.html?attacker_controlled=<svg onload=alert(1)>
```
But there are many other possibilities. Be sure to check the following sections to learn more about what to expect and how to test different scenarios:

- "Cross-Site Scripting Flaws".
- "Injection Flaws".
- "Testing Object Persistence".
- "Testing for URL Loading in WebViews"
- "Testing JavaScript Execution in WebViews"
- "Testing WebView Protocol Handlers"

In addition, we recommend to search and read public reports (search term: `"deep link*"|"deeplink*" site:https://hackerone.com/reports/`). For example:

- "[HackerOne#1372667](https://hackerone.com/reports/1372667) Able to steal bearer token from deep link" 
- "[HackerOne#401793](https://hackerone.com/reports/401793) Insecure deeplink leads to sensitive information disclosure" 
- "[HackerOne#583987](https://hackerone.com/reports/583987) Android app deeplink leads to CSRF in follow action" 
- "[HackerOne#637194](https://hackerone.com/reports/637194) Bypass of biometrics security functionality is possible in Android application" 
- "[HackerOne#341908](https://hackerone.com/reports/341908) XSS via Direct Message deeplinks" 