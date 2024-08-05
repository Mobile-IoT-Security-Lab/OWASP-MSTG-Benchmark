# [MASTG-TEST-0001: Testing Local Storage for Sensitive Data](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0001)

## Note

- sharedpreference dovrebbe usare mode world readable or writable
- bisognerebbe includere hardcoded cryptographic keys come da esempio MASTG


## Implementation
- 
    - creato app che salva Username e pwd all’interno delle shared preferences.
    - al click del button vengono recuperate le credenziali dalle shared preferences e dispayate.
    - nel `AndroidManifest.xml` aggiunto permesso per scrivere in external storage
        
        `<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>`
        
    - Nella `MainActivity` aggiunto salvataggio di informazioni sensibili nelle SharedPreferences attraverso un form .
    - Aggiunto un valore in `string.xml`
    
## Overview

This test case focuses on identifying potentially sensitive data stored by an application and verifying if it is securely stored. The following checks should be performed:

- Analyze data storage in the source code.
- Be sure to trigger all possible functionalities of the application (e.g., by clicking everywhere possible) in order to ensure data generation.
- Review the application’s shared preferences files and ensure that they store nothing of sufficient security concern.
- Review the application’s databases, shared preferences, and temporary files.

**NOTE:** For MAM/GMS I compliance, it is sufficient to store data encrypted in the application’s internal storage directory (sandbox). For L2 compliance, additional encryption is required using cryptographic keys securely managed in the Android Keystore. This includes using envelope encryption (AES-256) or equivalent methods, or using the Android Security Library’s [EncryptedFile API](https://developer.android.com/reference/androidx/security/crypto/EncryptedFile).

MASVS-STORAGE-1 / MSTG-STORAGE-1 / MSTG-STORAGE-2 / March 01, 2024
## Static Analysis

First of all, try to determine the kind of storage used by the Android app and find where the app processes sensitive data insecurely.

- Check `AndroidManifest.xml` for hardcoded external storage permissions, for example, `android.permission.WRITE_EXTERNAL_STORAGE`.
- Check the source code for keywords and APIs that can be used to store data:
  - `getExternalFilesDir()`
  - `getExternalStoragePublicDirectory()`
  - Avoid using `MODE_WORLD_READABLE` and `MODE_WORLD_WRITABLE` for files because any app will be able to read from or write to the files, even if they are stored in the app’s private data directory. If data must be shared with other applications, consider a content provider. A content provider offers read and write permissions to other apps and can grant dynamic permission on a case-by-case basis.
- Classes and functions, such as:
  - The `Environment` class (Stores by variable path)
  - The `SharedPreferences` class (internal or external storage)
  - The `getExternalFilesDir` function (Stores external storage)
  - The `getFilesDir` and `getCacheDir` functions (internal storage)
  - The `openFileOutput` and `openFileInput` functions (file creation/database for writing)
  - The `SQLiteDatabase` and `getReadableDatabase` and `getWritableDatabase` functions (file creation/database for reading)
  - The `getExternalStoragePublicDirectory` function (Stores cached files)

Encryption should be implemented using proven SDK functions. The following describes bad practices to look for in the source code:

- Locally stored sensitive information "encrypted" with simple techniques like XOR or bit flipping. These operations should be avoided because the encrypted data can be recovered easily.
- Keys stored or created without Android code-based features, such as the Android Keystore.
- Keys disclosed by hardcoding.

A typical issue arises when hardcoded cryptographic keys. Hard-coded and world-readable cryptographic keys significantly increase the possibility that encrypted data will be recovered. Once an attacker obtains the data, decrypting it is trivial. Symmetric cryptography keys must be stored on the device, so identifying them is just a matter of time and effort. Consider the following code:
```
this.db = localUserSecretStore.getWritableDatabase("SuperPassword123");
```
Obtaining the key is trivial because it is contained in the source code and identical for all installations of the app. Encrypting data this way is not beneficial. Look for hard-coded API keys/private keys and other valuable data; they pose a similar risk. Encoded/encrypted keys represent another attempt to make it harder but not impossible to get the crown jewels.

Consider the following code:

Example in Java:
```
//A more complicated effort to store the XOR'ed halves of a key (instead of the key itself)
private static final String[] myCompositeKey = new String[]{
  "oNQavjbaNNSgEqoCkT9Em4imeQQ=","3o8eFOX4ri/F8fgHgiy/BS47"
};
```
Example in Kotlin:
```
private val myCompositeKey = arrayOf<String>("oNQavjbaNNSgEqoCkT9Em4imeQQ=", "3o8eFOX4ri/F8fgHgiy/BS47")
```
The algorithm for decoding the original key might be something like this:

Example in Java:
```
public void useXorStringHiding(String myHiddenMessage) {
  byte[] xorParts0 = Base64.decode(myCompositeKey[0],0);
  byte[] xorParts1 = Base64.decode(myCompositeKey[1],0);

  byte[] xorKey = new byte[xorParts0.length];
  for(int i = 0; i < xorParts1.length; i++){
    xorKey[i] = (byte) (xorParts0[i] ^ xorParts1[i]);
  }
  HidingUtil.doHiding(myHiddenMessage.getBytes(), xorKey, false);
}
```
Example in Kotlin:
```
fun useXorStringHiding(myHiddenMessage:String) {
  val xorParts0 = Base64.decode(myCompositeKey[0], 0)
  val xorParts1 = Base64.decode(myCompositeKey[1], 0)
  val xorKey = ByteArray(xorParts0.size)
  for (i in xorParts1.indices)
  {
    xorKey[i] = (xorParts0[i] xor xorParts1[i]).toByte()
  }
  HidingUtil.doHiding(myHiddenMessage.toByteArray(), xorKey, false)
}
```
Verify common locations of secrets:

resources (typically at res/values/strings.xml) Example:
```
<resources>
    <string name="app_name">SuperApp</string>
    <string name="hello_world">Hello world!</string>
    <string name="action_settings">Settings</string>
    <string name="secret_key">My_Secret_Key</string>
  </resources>
  ```

build configs, such as in local.properties or gradle.properties Example:
```
buildTypes {
  debug {
    minifyEnabled true
    buildConfigField "String", "hiddenPassword", "\"${hiddenPassword}\""
  }
}
```