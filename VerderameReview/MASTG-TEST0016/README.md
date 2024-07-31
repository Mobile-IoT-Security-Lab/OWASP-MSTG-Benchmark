# [MASTG-TEST-0016: Testing Random Number Generation](https://mas.owasp.org/MASTG/tests/android/MASVS-CRYPTO/MASTG-TEST-0016)

## Implementation

- Creato registration / login app
- le credenziali vengono salvate e controllate in un file di testo all’interno della memoria interna `credentials.txt`
- quando viene fatto login viene generato un token di sessione usando la funzione di `Java.Util.random` , questo token viene associato allo username e viene salvato nelle shared preferences.

## Overview
MASVS-CRYPTO-1 / MSTG-CRYPTO-6 / June 02, 2024
## Static Analysis
Identify all the instances of random number generators and look for either custom or well-known insecure classes. For instance, java.util.Random produces an identical sequence of numbers for each given seed value; consequently, the sequence of numbers is predictable. Instead a well-vetted algorithm should be chosen that is currently considered to be strong by experts in the field, and a well-tested implementations with adequate length seeds should be used.

Identify all instances of SecureRandom that are not created using the default constructor. Specifying the seed value may reduce randomness. Use only the no-argument constructor of SecureRandom  that uses the system-specified seed value to generate a 128-byte-long random number.

In general, if a PRNG is not advertised as being cryptographically secure (e.g. java.util.Random), then it is probably a statistical PRNG and should not be used in security-sensitive contexts. Pseudo-random number generators can produce predictable numbers  if the generator is known and the seed can be guessed. A 128-bit seed is a good starting point for producing a "random enough" number.

Once an attacker knows what type of weak pseudo-random number generator (PRNG) is used, it can be trivial to write a proof-of-concept to generate the next random value based on previously observed ones, as it was done for Java Random . In case of very weak custom random generators it may be possible to observe the pattern statistically. Although the recommended approach would anyway be to decompile the APK and inspect the algorithm (see Static Analysis).

If you want to test for randomness, you can try to capture a large set of numbers and check with the Burp's sequencer  to see how good the quality of the randomness is.