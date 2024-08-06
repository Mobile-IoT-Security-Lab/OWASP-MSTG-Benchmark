# [MASTG-TEST-0022: Testing Custom Certificate Stores and Certificate Pinning](https://mas.owasp.org/MASTG/tests/android/MASVS-NETWORK/MASTG-TEST-0022)

## Nota

Implementazione test parziale
da rivedere la vulnerabilità


## Implementation 

- creato app con due bottini:
    - SSL pinning
        - manda richiesta a [www.example.com](http://www.example.com) pinnando I certificate create([https://gist.github.com/pollux-/696657b75b45f1755f9105580b125c0](https://gist.github.com/pollux-/696657b75b45f1755f9105580b125c08)) ed aggiunti nel `network_security_config.xml`
        - restituisce un Toast
            - OK
            - Errore
    - No SSL pinning
        - manda richiesta a [www.example.com](http://www.example.com) pinnando I certificati
        - restituisce un Toast
            - OK
            - Errore
            
## Overview
MASVS-NETWORK-2 / MSTG-NETWORK-4 / April 27, 2024
## Static Analysis

## Network Security Configuration

Inspect the Network Security Configuration looking for any `<pin-set>` elements. Check their expiration date, if any. If expired, certificate pinning will be disabled for the affected domains.

Testing Tip: If a certificate pinning validation check has failed, the following event should be logged in the system logs:

```
I/X509Util: Failed to validate the certificate chain, error: Pin verification failed
```

## TrustManager

Implementing certificate pinning involves three main steps:

- Obtain the certificate of the desired host(s).
- Make sure the certificate is in .bks format.
- Pin the certificate to an instance of the default Apache Httpclient.

To analyze the correct implementation of certificate pinning, the HTTP client should load the KeyStore:

```
InputStream in = resources.openRawResource(certificateRawResource);
keyStore = KeyStore.getInstance("BKS");
keyStore.load(resourceStream, password);
```

Once the KeyStore has been loaded, we can use the TrustManager that trusts the CAs in our KeyStore:

```
String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
tmf.init(keyStore);
// Create an SSLContext that uses the TrustManager
// SSLContext context = SSLContext.getInstance("TLS");
sslContext.init(null, tmf.getTrustManagers(), null);
```

The app's implementation may be different, pinning against the certificate's public key only, the whole certificate, or a whole certificate chain.

## Network Libraries and WebViews

Applications that use third-party networking libraries may utilize the libraries' certificate pinning functionality. For example,  [okhttp ↗](https://github.com/square/okhttp) can be set up with the CertificatePinner as follows:

```
OkHttpClient client = new OkHttpClient.Builder()
        .certificatePinner(new CertificatePinner.Builder()
            .add("example.com", "sha256/UwQAapahrjCOjYI3oLUx5AQxPBR02Jz6/E2pt0IeLXA=")
            .build())
        .build();
```

Applications that use a WebView coSample Xamarin app with the previous example can be obtained on the MASTG repository 
mponent may utilize the WebViewClient's event handler for some kind of "certificate pinning" of each request before the target resource is loaded. The following code shows an example verification:

```
WebView myWebView = (WebView) findViewById(R.id.webview);
myWebView.setWebViewClient(new WebViewClient(){
    private String expectedIssuerDN = "CN=Let's Encrypt Authority X3,O=Let's Encrypt,C=US;";

    @Override
    public void onLoadResource(WebView view, String url)  {
        //From Android API documentation about "WebView.getCertificate()":
        //Gets the SSL certificate for the main top-level page
        //or null if there is no certificate (the site is not secure).
        //
        //Available information on SslCertificate class are "Issuer DN", "Subject DN" and validity date helpers
        SslCertificate serverCert = view.getCertificate();
        if(serverCert != null){
            //apply either certificate or public key pinning comparison here
                //Throw exception to cancel resource loading...
            }
        }
    }
});
```

Alternatively, it is better to use an OkHttpClient with configured pins and let it act as a proxy overriding shouldInterceptRequest of the WebViewClient.

## Xamarin Applications

Applications developed in Xamarin will typically use ServicePointManager to implement pinning.

Normally a function is created to check the certificate(s) and return the boolean value to the method `ServerCertificateValidationCallback`:

```
[Activity(Label = "XamarinPinning", MainLauncher = true)]
    public class MainActivity : Activity
    {
        // SupportedPublicKey - Hexadecimal value of the public key.
        // Use GetPublicKeyString() method to determine the public key of the certificate we want to pin. Uncomment the debug code in the ValidateServerCertificate function a first time to determine the value to pin.
        private const string SupportedPublicKey = "3082010A02820101009CD30CF05AE52E47B7725D3783B..."; // Shortened for readability

        private static bool ValidateServerCertificate(
                object sender,
                X509Certificate certificate,
                X509Chain chain,
                SslPolicyErrors sslPolicyErrors
            )
        {
            //Log.Debug("Xamarin Pinning",chain.ChainElements[X].Certificate.GetPublicKeyString());
            //return true;
            return SupportedPublicKey == chain.ChainElements[1].Certificate.GetPublicKeyString();
        }

        protected override void OnCreate(Bundle savedInstanceState)
        {
            System.Net.ServicePointManager.ServerCertificateValidationCallback += ValidateServerCertificate;
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.Main);
            TesteAsync("https://security.claudio.pt");

        }
```

In this particular example we are pinning the intermediate CA of the certificate chain. The output of the HTTP response will be available in the system logs.

Sample Xamarin app with the previous example can be obtained on the MASTG repository ↗

After decompressing the APK file, use a .NET decompiler like dotPeak, ILSpy or dnSpy to decompile the app dlls stored inside the 'Assemblies' folder and confirm the usage of the ServicePointManager.

Learn more:

- Certificate and Public Key Pinning with Xamarin - https://thomasbandt.com/certificate-and-public-key-pinning-with-xamarin ↗

- ServicePointManager - https://msdn.microsoft.com/en-us/library/system.net.servicepointmanager(v=vs.110).aspx ↗

## Cordova Applications
Hybrid applications based on Cordova do not support Certificate Pinning natively, so plugins are used to achieve this. The most common one is [PhoneGap SSL Certificate Checker ↗](https://github.com/EddyVerbruggen/SSLCertificateChecker-PhoneGap-Plugin). The check method is used to confirm the fingerprint and callbacks will determine the next steps.

```
  // Endpoint to verify against certificate pinning.
  var server = "https://www.owasp.org";
  // SHA256 Fingerprint (Can be obtained via "openssl s_client -connect hostname:443 | openssl x509 -noout -fingerprint -sha256"
  var fingerprint = "D8 EF 3C DF 7E F6 44 BA 04 EC D5 97 14 BB 00 4A 7A F5 26 63 53 87 4E 76 67 77 F0 F4 CC ED 67 B9";

  window.plugins.sslCertificateChecker.check(
          successCallback,
          errorCallback,
          server,
          fingerprint);

   function successCallback(message) {
     alert(message);
     // Message is always: CONNECTION_SECURE.
     // Now do something with the trusted server.
   }

   function errorCallback(message) {
     alert(message);
     if (message === "CONNECTION_NOT_SECURE") {
       // There is likely a man in the middle attack going on, be careful!
     } else if (message.indexOf("CONNECTION_FAILED") >- 1) {
       // There was no connection (yet). Internet may be down. Try again (a few times) after a little timeout.
     }
   }

```

After decompressing the APK file, Cordova/Phonegap files will be located in the /assets/www folder. The 'plugins' folder will give you the visibility of the plugins used. We will need to search for this methods in the JavaScript code of the application to confirm its usage.
