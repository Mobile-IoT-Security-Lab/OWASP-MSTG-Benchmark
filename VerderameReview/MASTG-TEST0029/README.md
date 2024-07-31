# [MASTG-TEST-0029: Testing for Sensitive Functionality Exposure Through IPC](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0029)

## Implementation

- create PWD Manager a cui si puo accedere tramite Login che controlla credenziali attraverso file `credentials.txt` (admin,1234)
- `PwdManager.class` contiene tutte le password salvate su `pwd.txt` e da la possibilità di salvare nuove password che verranno displayate nell’attività
- La vulnerabiltà è present nel manifest che definisce l’attività PwdManager come `android:exported="true"`
- In questo modo da terminale è possibile aprire l’attivita `PwdManager` senza passare dal login.

## Overview

To test for [sensitive functionality exposure through IPC](https://owasp.org/www-project-mobile-security-testing-guide/mas/0x07a-Testing-for-Sensitive-Functionality-Exposure-Through-IPC.html) mechanisms you should first enumerate all the IPC mechanisms the app uses and then try to identify whether sensitive data is leaked when the mechanisms are used.

MASVS-PLATFORM-1 / MSTG-PLATFORM-4 / May 08, 2023

## Static Analysis

We start by looking at the AndroidManifest.xml, where all activities, services, and content providers included in the app must be declared (otherwise the system won’t recognize them and they won’t run).

- [`<intent-filter>`](https://developer.android.com/guide/topics/manifest/intent-filter-element)
- [`<service>`](https://developer.android.com/guide/topics/manifest/service-element)
- [`<provider>`](https://developer.android.com/guide/topics/manifest/provider-element)
- [`<receiver>`](https://developer.android.com/guide/topics/manifest/receiver-element)

An “exported” activity, service, or content can be accessed by other apps. There are two common ways to designate a component as exported. The obvious one is setting the export tag to true `android:exported="true"`. The second way involves defining an `<intent-filter>` within the component element (`<activity>`, `<service>`, `<receiver>`). When this is done, the export tag is automatically set to "true". To prevent all other Android apps from interacting with the IPC component element, be sure that the `android:exported="true"` value and an `<intent-filter>` aren’t in their AndroidManifest.xml files unless this is necessary.

Remember that using the permission tag (`android:permission`) will also limit other applications' access to a component. If your IPC is intended to be accessible to other applications, you can apply a security policy with the `<permission>` element and set a proper `android:protectionLevel`. When `android:permission` is used in a service declaration, other applications must declare a corresponding `<uses-permission>` element in their own manifest to start, stop, or bind to the service.

For more information about the content providers, please refer to the test case "Testing Whether Stored Sensitive Data Is Exposed via IPC Mechanisms" in chapter "Testing Data Storage".

Once you identify a list of IPC mechanisms, review the source code to see whether sensitive data is leaked when the mechanisms are used. For example, content providers can be used to access database information, and services can be probed to see if they return data. Broadcast receivers can leak sensitive information if probed or sniffed.

In the following, we use two example apps and give examples of identifying vulnerable IPC components:

- [“Sieve”](https://github.com/nowsecure/sieve)
- [“Android Insecure Bank”](https://github.com/dineshshetty/Android-InsecureBankv2)

## Activities

### Inspect the AndroidManifest

In the “Sieve” app, we find three exported activities, identified by `<activity>`:

```
<activity android:excludeFromRecents="true" android:label="@string/app_name" android:launchMode="singleTask" android:name=".MainLoginActivity" android:windowSoftInputMode="adjustResize|stateVisible">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
<activity android:clearTaskOnLaunch="true" android:excludeFromRecents="true" android:exported="true" android:finishOnTaskLaunch="true" android:label="@string/title_activity_file_select" android:name=".FileSelectActivity" />
<activity android:clearTaskOnLaunch="true" android:excludeFromRecents="true" android:exported="true" android:finishOnTaskLaunch="true" android:label="@string/title_activity_pwlist" android:name=".PWList" />
```
### Inspect source code
By inspecting the `PWList.java` activity, we see that it offers options to list all keys, add, delete, etc. If we invoke it directly, we will be able to bypass the LoginActivity. More on this can be found in the dynamic analysis below.

## Services
### Inspect the AndroidManifestùù

In the "Sieve" app, we find two exported services, identified by `<service>`:

```
<service android:exported="true" android:name=".AuthService" android:process=":remote" />
<service android:exported="true" android:name=".CryptoService" android:process=":remote" />
```
### Inspect the Source Code
Check the source code for the class `android.app.Service`:

By reversing the target application, we can see that the service AuthService provides functionality for changing the password and PIN-protecting the target app.

```
   public void handleMessage(Message msg) {
            AuthService.this.responseHandler = msg.replyTo;
            Bundle returnBundle = msg.obj;
            int responseCode;
            int returnVal;
            switch (msg.what) {
                ...
                case AuthService.MSG_SET /*6345*/:
                    if (msg.arg1 == AuthService.TYPE_KEY) /*7452*/ {
                        responseCode = 42;
                        if (AuthService.this.setKey(returnBundle.getString("com.mwr.example.sieve.PASSWORD"))) {
                            returnVal = 0;
                        } else {
                            returnVal = 1;
                        }
                    } else if (msg.arg1 == AuthService.TYPE_PIN) {
                        responseCode = 41;
                        if (AuthService.this.setPin(returnBundle.getString("com.mwr.example.sieve.PIN"))) {
                            returnVal = 0;
                        } else {
                            returnVal = 1;
                        }
                    } else {
                        sendUnrecognisedMessage();
                        return;
                    }
           }
   }
```
## Broadcast Receivers
### Inspect the Android Manifest
In the "Android Insecure Bank" app, we find a broadcast receiver in the manifest, identified by `<receiver>`:

```

<receiver android:exported="true" android:name="com.android.insecurebankv2.MyBroadCastReceiver">
    <intent-filter>
        <action android:name="theBroadcast" />
    </intent-filter>
</receiver>
```

### Inspect the Source Code
Search the source code for strings like `sendBroadcast, sendOrderedBroadcast, and sendStickyBroadcast`. Make sure that the application doesn't send any sensitive data.

If an Intent is broadcasted and received within the application only, `LocalBroadcastManager `can be used to prevent other apps from receiving the broadcast message. This reduces the risk of leaking sensitive information.

To understand more about what the receiver is intended to do, we have to go deeper in our static analysis and search for usage of the class `android.content.BroadcastReceiver `and the `Context`.`registerReceiver` method, which is used to dynamically create receivers.

The following extract of the target application's source code shows that the broadcast receiver triggers transmission of an SMS message containing the user's decrypted password.

```

public class MyBroadCastReceiver extends BroadcastReceiver {
  String usernameBase64ByteString;
  public static final String MYPREFS = "mySharedPreferences";

  @Override
  public void onReceive(Context context, Intent intent) {
    // TODO Auto-generated method stub

        String phn = intent.getStringExtra("phonenumber");
        String newpass = intent.getStringExtra("newpass");

    if (phn != null) {
      try {
                SharedPreferences settings = context.getSharedPreferences(MYPREFS, Context.MODE_WORLD_READABLE);
                final String username = settings.getString("EncryptedUsername", null);
                byte[] usernameBase64Byte = Base64.decode(username, Base64.DEFAULT);
                usernameBase64ByteString = new String(usernameBase64Byte, "UTF-8");
                final String password = settings.getString("superSecurePassword", null);
                CryptoClass crypt = new CryptoClass();
                String decryptedPassword = crypt.aesDeccryptedString(password);
                String textPhoneno = phn.toString();
                String textMessage = "Updated Password from: "+decryptedPassword+" to: "+newpass;
                SmsManager smsManager = SmsManager.getDefault();
                System.out.println("For the changepassword - phonenumber: "+textPhoneno+" password is: "+textMessage);
smsManager.sendTextMessage(textPhoneno, null, textMessage, null, null);
          }
     }
  }
}

```

BroadcastReceivers should use the `android:permission `attribute; otherwise, other applications can invoke them. You can use Context.`sendBroadcast(intent, receiverPermission); `to specify permissions a receiver must have to [read the broadcast ↗](https://developer.android.com/reference/android/content/Context#sendBroadcast%28android.content.Intent,%20java.lang.String%29). You can also set an explicit application package name that limits the components this Intent will resolve to. If left as the default value (null), all components in all applications will be considered. If non-null, the Intent can match only the components in the given application package.

