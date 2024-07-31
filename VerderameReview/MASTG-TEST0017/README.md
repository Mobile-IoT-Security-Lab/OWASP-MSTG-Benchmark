# [MASTG-TEST-0017: Testing Confirm Credentials](https://mas.owasp.org/MASTG/tests/android/MASVS-AUTH/MASTG-TEST-0017/)

## Implementation

- Creato app with simple login/logout (admin) , nelle shared preference allo user name viene associato un flag `isLoggedIn` (boolean).
- se si inserisce lo user name corretto si viene autenticati andando a modificare il valore di `isLoggedIn` nelle shared preferences.
- Sfruttare vulnerabilità:
    - cambiare valore delle shared preferences mettendo il login a true, in questo modo si riesce a sfruttare

## Overview
MASVS-AUTH-2 / MSTG-AUTH-1 / MSTG-STORAGE-11 / May 08, 2023
## Static Analysis
Make sure that the unlocked key is used during the application flow. For example, the key may be used to decrypt local storage or a message received from a remote endpoint. If the application simply checks whether the user has unlocked the key or not, the application may be vulnerable to a local authentication bypass.