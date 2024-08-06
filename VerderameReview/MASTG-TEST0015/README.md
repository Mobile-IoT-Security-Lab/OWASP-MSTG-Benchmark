# [MASTG-TEST-0015: Testing the Purposes of Keys](https://mas.owasp.org/MASTG/tests/android/MASVS-CRYPTO/MASTG-TEST-0015)

## Note

Se la chiave è simmetrica vuol dire che viene usata sia per decifrare che per cifrare in uno stesso contesto; la vulnerabilità sarebbe se viene usata in più di un contesto!

Rivedere quindi i risultati accordingly

## Implementation

- creare app di registrazion e login che usa encrypt con AES con key harcoded nella classe Crypt  .
- Questo frammento di codice mostra un'app Android vulnerabile che genera una chiave simmetrica e la utilizza sia per la crittografia che per la decrittografia. Ciò viola il principio dell'utilizzo delle chiavi per scopi specifici. In un'applicazione sicura, è necessario utilizzare chiavi separate per la crittografia e la decrittografia.
- Per migliorare la sicurezza di questa app, dovresti generare chiavi separate per la crittografia e la decrittografia e garantire che ciascuna chiave venga utilizzata solo per lo scopo previsto. Inoltre, è necessario implementare un'adeguata gestione degli errori e il rilevamento delle eccezioni.

## Overview
MASVS-CRYPTO-2 / MSTG-CRYPTO-5 / May 13, 2024
## Static Analysis

Identify all instances where cryptography is used. You can look for:

- classes `Cipher`, `Mac`, `MessageDigest`, `Signature`
- interfaces `Key`, `PrivateKey`, `PublicKey`, `SecretKey`
- functions `getInstance`, `generateKey`
- exceptions `KeyStoreException`, `CertificateException`, `NoSuchAlgorithmException`
- classes importing `java.security.*`, `javax.crypto.*`, `android.security.*`, `android.security.keystore.*`

For each identified instance, identify its purpose and its type. It can be used:

- for encryption/decryption - to ensure data confidentiality
- for signing/verifying - to ensure integrity of data (as well as accountability in some cases)
- for maintenance - to protect keys during certain sensitive operations (such as being imported to the KeyStore)

Additionally, you should identify the business logic which uses identified instances of cryptography.

During verification the following checks should be performed:

- are all keys used according to the purpose defined during its creation? (it is relevant to KeyStore keys, which can have KeyProperties defined)
- for asymmetric keys, is the private key being exclusively used for signing and the public key encryption?
- are symmetric keys used for multiple purposes? A new symmetric key should be generated if it's used in a different context.
- is cryptography used according to its business purpose?