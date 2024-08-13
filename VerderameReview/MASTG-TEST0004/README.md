# [MASTG-TEST-0004: Determining Whether Sensitive Data Is Shared with Third Parties via Embedded Services](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0004)

## Note

- Non mi è chiaro come APKHunt trovi la vulnerabilità
- includere libreria che serve per inviare dati (versione vulnerabile), cifrare alcuni dati altri no (email e password);
- interfaccia nativa per raccogliere user e password


## Implementation 

- creato semplice form html che manda richiesta POST a flask API che controlla solamente che i dati mandati non siano vuoti.
- aggiunto network_security_config.xml:
    
    ```xml
    
        <?xml version="1.0" encoding="utf-8"?>
        <network-security-config>
            <domain-config cleartextTrafficPermitted="true">
                <domain includeSubdomains="true">10.0.2.2</domain>
            </domain-config>
        </network-security-config>
    ```
    
- In tal caso si viene reindirizzati ad un’altra pagina web .
- la nostra app mobile contiene un form che manda i dati attraverso all’API
- Spiegazione:
    - Questo script richiede all'utente di inserire il proprio nome ed e-mail.
    - Invia quindi questi dati direttamente a un endpoint API di terze parti senza alcuna anonimizzazione.
    - Se un utente malintenzionato intercetta questa comunicazione, può facilmente ottenere il nome e l'e-mail dell'utente, che sono considerati Informazioni Personali.

Per migliorare la sicurezza di questa applicazione, dovresti implementare adeguate tecniche di anonimizzazione come l'hashing o la crittografia delle Informazioni personali prima di inviarle al servizio di terze parti. Inoltre, dovresti rivedere la documentazione del servizio di terze parti per garantire la conformità alle migliori pratiche e alle normative sulla protezione dei dati

Per attivare `ThirdPart-MASTG-TEST0004`

```java
cd ThirdPart-MASTG-TEST0004
flask --app app run
```

## Overview
MASVS-STORAGE-2 / MSTG-STORAGE-4 / May 08, 2023
## Static Analysis

To determine whether API calls and functions provided by the third-party library are used according to best practices, review their source code, requested permissions and check for any known vulnerabilities.

All data that's sent to third-party services should be anonymized to prevent exposure of PII (Personal Identifiable Information) that would allow the third party to identify the user account. No other data (such as IDs that can be mapped to a user account or session) should be sent to a third party.