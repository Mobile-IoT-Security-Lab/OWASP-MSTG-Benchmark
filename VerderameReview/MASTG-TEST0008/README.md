# [MASTG-TEST-0008: Checking for Sensitive Data Disclosure Through the User Interface](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0008)

## Implementation

- Creato shop che chiede in input Credit Card Number e Pin i quali non vengono offuscati mentre vengono scritti , schiacciando il bottone buy a coffe viene inviata una notifica con CN e Pin in chiaro

Unione di [Determining Whether the Keyboard Cache Is Disabled for Text Input Fields](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0006/) di [Determining Whether Sensitive Data Is Shared with Third Parties via Embedded Services](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0005/)

## Overview
MASVS-PLATFORM-3 / MSTG-STORAGE-7 / May 08, 2023
## Static Analysis
Carefully review all UI components that either show such information or take it as input. Search for any traces of sensitive information and evaluate if it should be masked or completely removed.

## Text Fields
To make sure an application is masking sensitive user input, check for the following attribute in the definition of `EditText`:
```
android:inputType="textPassword"
```

With this setting, dots (instead of the input characters) will be displayed in the text field, preventing the app from leaking passwords or pins to the user interface.

## App Notifications
When statically assessing an application, it is recommended to search for any usage of the NotificationManager class which might be an indication of some form of notification management. If the class is being used, the next step would be to understand how the application is generating the notifications.

These code locations can be fed into the Dynamic Analysis section below, providing an idea of where in the application notifications may be dynamically generated.

