# [MASTG-TEST-0013: Testing Symmetric Cryptography](https://mas.owasp.org/MASTG/tests/android/MASVS-CRYPTO/MASTG-TEST-0013)

## Implementation

- creata app che crypta e decrypta un testo utilizzando AES
- Possiamo chiaramente individuare l'uso di una chiave di cifratura statica che è codificata direttamente e inizializzata nell'array di byte statico `keyBytes` .

## Overview
MASVS-CRYPTO-1 / MSTG-CRYPTO-1 / May 13, 2024
## Static  Analysis
Identify all the instances of symmetric key encryption in code and look for any mechanism which loads or provides a symmetric key. You can look for:

- symmetric algorithms (such as `DES`, `AES`, etc.)
- specifications for a key generator (such as `KeyGenParameterSpec`, `KeyPairGeneratorSpec`, `KeyPairGenerator`, `KeyGenerator`, `KeyProperties`, etc.)
- classes importing `java.security.*`, `javax.crypto.*`, `android.security.*`, `android.security.keystore.*`

Check also the [list of common cryptographic configuration issues](https://github.com/google/android-emulator-container-scripts).

For each identified instance verify if the used symmetric keys:

- are not part of the application resources
- cannot be derived from known values
- are not hardcoded in code

For each hardcoded symmetric key, verify that is not used in security-sensitive contexts as the only method of encryption.

As an example we illustrate how to locate the use of a hardcoded encryption key. First disassemble and decompile the app to obtain Java code, e.g. by using [jadx](https://github.com/skylot/jadx).

Now search the files for the usage of the `SecretKeySpec` class, e.g. by simply recursively grepping on them or using jadx search function:

```
grep -r "SecretKeySpec"
```
This will return all classes using the SecretKeySpec class. Now examine those files and trace which variables are used to pass the key material. The figure below shows the result of performing this assessment on a production ready application. We can clearly locate the use of a static encryption key that is hardcoded and initialized in the static byte array Encrypt.keyBytes.

import javax.crypto.spec.*;
import java.security.*;
import javax.crypto.*;
import android.util.*;
```
public class Encrypt
{
    private static byte[] keyBytes;
    static {
        Encrypt.keyBytes = new byte[] { 7, 3, 4, 5, 6, 7, 8, 9, 10, 17, 18, 9, 20, 21, 15, 1, 10, 11, 12, 13, 14, 3 };
    }

    public static String encrypt(final String s) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(Encrypt.keyBytes, "AES");
        final Cipher instance = Cipher.getInstance("AES");
        instance.init(1, secretKeySpec);
        return new String(Base64.encode(instance.doFinal(s.getBytes()), 0));
    }

    public static String decrypt(final String s) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(Encrypt.keyBytes, "AES");
        final Cipher instance = Cipher.getInstance("AES");
        instance.init(2, secretKeySpec);
        return new String(Base64.encode(instance.doFinal(s.getBytes()), 0));
    }
}
```