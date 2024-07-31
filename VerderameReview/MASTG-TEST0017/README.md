# [MASTG-TEST-0017: Testing Confirm Credentials](https://mas.owasp.org/MASTG/tests/android/MASVS-AUTH/MASTG-TEST-0017/)
## Overview
MASVS-AUTH-2 / MSTG-AUTH-1 / MSTG-STORAGE-11 / May 08, 2023
## Static Analysis
Make sure that the unlocked key is used during the application flow. For example, the key may be used to decrypt local storage or a message received from a remote endpoint. If the application simply checks whether the user has unlocked the key or not, the application may be vulnerable to a local authentication bypass.