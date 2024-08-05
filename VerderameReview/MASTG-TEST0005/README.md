# [MASTG-TEST-0005: Determining Whether Sensitive Data Is Shared with Third Parties via Notifications](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0005)

## Note

- questo è il problema della sensitive data (da valutare un attimo)
- TrueSeeing la butta dentro il calderore del Detected Logging
- MobFS e ApkHunt trovano il notification Manager ma ovviamente non sa dire se i dati sono sensitive o no

## Implementation

- Creato SHOP che inserendo Credit Card Number e Pin schiacciando il bottone buy a coffe manda una notifica, dicendo che il caffè è stato comprato ma viene mostrato anche pin e numero di carta, il che rende insicura l’app.
- nel `AndroidManifest.xml` aggiunto permesso per scrivere in external storage
    
    `<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>`
    
- Nel `MainActivity` aggiunta creazione di una notifica secondo la guida:
    - https://developer.android.com/develop/ui/views/notifications/build-notification?hl=it

## Overview
MASVS-STORAGE-2 / MSTG-STORAGE-4 / May 08, 2023
## Static Analysis
Search for any usage of the NotificationManager class which might be an indication of some form of notification management. If the class is being used, the next step would be to understand how the application is generating the notifications  and which data ends up being shown.