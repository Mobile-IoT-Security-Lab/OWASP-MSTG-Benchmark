# [MASTG-TEST-0002: Testing Local Storage for Input Validation](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0002s)

## Implementation

In questo esempio, abbiamo un semplice sistema di accesso dove il nome utente dell'utente viene memorizzato utilizzando SharedPreferences.

Dopo un accesso riuscito(Username: admin), il flag isLoggedIn viene anche memorizzato per indicare se l'utente ha effettuato l'accesso o meno.
Tuttavia, non vi è alcuna validazione di input o controllo di integrità durante la lettura dei dati memorizzati da SharedPreferences. Questo rende l'applicazione vulnerabile agli attacchi di manipolazione dei dati, dove un attaccante potrebbe modificare il nome utente memorizzato o il flag isLoggedIn per ottenere accesso non autorizzato o manipolare il comportamento dell'applicazione.

Ho sfruttato la vulnerabilità andando a modificare I valori nelle shared preferences.

## Overview
For any publicly accessible data storage, any process can override the data. This means that input validation needs to be applied the moment the data is read back again.

Note: The same is true for private accessible data on a rooted device

MASVS-CODE-4 / MSTG-PLATFORM-2 / May 08, 2023


## Static analysis
## Using Shared Preferences
When you use the SharedPreferences.Editor to read or write int/boolean/long values, you cannot check whether the data is overridden or not. However: it can hardly be used for actual attacks other than chaining the values (e.g. no additional exploits can be packed which will take over the control flow). In the case of a String or a StringSet you should be careful with how the data is interpreted. Using reflection based persistence? Check the section on "Testing Object Persistence" for Android to see how it should be validated. Using the SharedPreferences.Editor to store and read certificates or keys? Make sure you have patched your security provider given vulnerabilities such as found in Bouncy Castle .

In all cases, having the content HMACed can help to ensure that no additions and/or changes have been applied.

## Using Other Storage Mechanisms
In case other public storage mechanisms (than the SharedPreferences.Editor) are used, the data needs to be validated the moment it is read from the storage mechanism.