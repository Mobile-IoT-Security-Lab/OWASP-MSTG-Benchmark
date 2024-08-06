# [MASTG-TEST-0037: Testing WebViews Cleanup](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0037)

## Nota 

rivedere l'applicazione perchè non sembra la vulnerabilità


## Implementation

- creato app che visualizza sito di https://talos-sec.com/
- l’app risulta vulnerable in quanto non applica le seguenti best practice;
    - **Initialization**: an app might be initializing the WebView in a way to avoid storing certain information by using `setDomStorageEnabled`, `setAppCacheEnabled` or `setDatabaseEnabled` from [`android.webkit.WebSettings` ↗](https://developer.android.com/reference/android/webkit/WebSettings). The DOM Storage (for using the HTML5 local storage), Application Caches and Database Storage APIs are disabled by default, but apps might set these settings explicitly to "true".
    - **Cache**: Android's WebView class offers the [`clearCache` ↗](https://developer.android.com/reference/android/webkit/WebView#clearCache(boolean)) method which can be used to clear the cache for all WebViews used by the app. It receives a boolean input parameter (`includeDiskFiles`) which will wipe all stored resource including the RAM cache. However if it's set to false, it will only clear the RAM cache. Check the app for usage of the `clearCache` method and verify its input parameter. Additionally, you may also check if the app is overriding `onRenderProcessUnresponsive` for the case when the WebView might become unresponsive, as the `clearCache` method might also be called from there.
    - **WebStorage APIs**: [`WebStorage.deleteAllData` ↗](https://developer.android.com/reference/android/webkit/WebStorage#deleteAllData) can be also used to clear all storage currently being used by the JavaScript storage APIs, including the Web SQL Database and the HTML5 Web Storage APIs.
        
        > Some apps will need to enable the DOM storage in order to display some HTML5 sites that use local storage. This should be carefully investigated as this might contain sensitive data.
        > 
    - **Cookies**: any existing cookies can be deleted by using [CookieManager.removeAllCookies ↗](https://developer.android.com/reference/android/webkit/CookieManager#removeAllCookies(android.webkit.ValueCallback%3Cjava.lang.Boolean%3E)).
    - **File APIs**: proper data deletion in certain directories might not be that straightforward, some apps use a pragmatic solution which is to *manually* delete selected directories known to hold user data. This can be done using the `java.io.File` API such as [`java.io.File.deleteRecursively` ↗](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.io/java.io.-file/delete-recursively.html).
    
## Overview

To test for [WebViews cleanup](https://developer.android.com/guide/webapps/managing-webview) you should inspect all APIs related to WebView data deletion and try to fully track the data deletion process.
MASVS-PLATFORM-2 / MSTG-PLATFORM-10 / May 08, 2023
## Static Analysis

Start by identifying the usage of the following WebView APIs and carefully validate the mentioned best practices.

- **Initialization**: an app might be initializing the WebView in a way to avoid storing certain information by using `setDomStorageEnabled`, `setAppCacheEnabled` or `setDatabaseEnabled` from `android.webkit.WebSettings`. The DOM Storage (for using the HTML5 local storage), Application Caches and Database Storage APIs are disabled by default, but apps might set these settings explicitly to "true".
- **Cache**: Android's WebView class offers the `clearCache` method which can be used to clear the cache for all WebViews used by the app. It receives a boolean input parameter (`includeDiskFiles`) which will wipe all stored resource including the RAM cache. However if it’s set to false, it will only clear the RAM cache. Check the app for usage of the `clearCache` method and verify its input parameter. Additionally, you may also check if the app is overriding `onRenderProcessUnresponsive` to see if the WebView might become unresponsive, as the `clearCache` method might also be called from there.
- **WebStorage APIs**: `WebStorage.deleteAllData` can be also used to clear all storage currently being used by the JavaScript storage APIs, including the Web SQL Database and the HTML5 Web Storage APIs. 
  - Some apps will need to enable the DOM storage in order to display some HTML5 sites that use local storage. This should be carefully investigated as this might contain sensitive data.
- **Cookies**: any existing cookies can be deleted by using `CookieManager.removeAllCookies`.
- **File APIs**: proper data deletion in certain directories might not be that straightforward, some apps use a pragmatic solution which is to manually delete selected directories known to hold user data. This can be done using the `java.io.File` API such as `java.io.File.deleteRecursively`.

### Example

This example in Kotlin from the open source [Firefox Focus](https://github.com/mozilla-mobile/focus-android) app shows different cleanup steps:

```
override fun cleanup() {
    clearFormData() // Removes the autocomplete popup from the currently focused form field, if present. Note this only affects the display of the autocomplete popup, it does not remove any stored data.
    clearHistory()
    clearMatches()
    clearSpPreferences()
    clearCache(true)
    
    CookieManager.getInstance().removeAllCookies(null)
    
    WebStorage.getInstance().deleteAllData() // Clears all storage currently being used by the JavaScript storage APIs. This includes the Application Cache, Web SQL Database and the HTML5 Web Storage APIs.
    
    val webViewDatabase = WebViewDatabase.getInstance(context)
    // It isn’t entirely clear how this differs from WebView.clearFormData()
    @Suppress("DEPRECATION")
    webViewDatabase.clearFormData() // Clears any saved data for web forms
    webViewDatabase.clearHttpAuthUsernamePassword()
    
    deleteContentFromKnownLocations(context) // Calls FileUtils.deleteWebViewDirectory(context) which deletes all content in “app_webview”.
}
```
The function finishes with some extra manual file deletion in deleteContentFromKnownLocations which calls functions from FileUtils. These functions use the java.io.File.deleteRecursively method to recursively delete files from the specified directories.
```
private fun deleteContentFromDirectory(directory: File, doNotEraseHtmlFiles: Set<String> = emptySet()): Boolean {
    val filesToDelete = directory.listFiles()?.filter { !doNotEraseHtmlFiles.contains(it.name) } ?: return false
    return filesToDelete.all { it.deleteRecursively() }
}

```