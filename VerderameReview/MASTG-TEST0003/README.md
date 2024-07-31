# [MASTG-TEST-0003: Testing Logs for Sensitive Data](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0003)

## Overview

This test case focuses on identifying any sensitive application data within both system and application logs. The following checks should be performed:

- Analyze source code for logging related code.
- Check application data directory for log files.
- Gather system messages and logs and analyze for any sensitive data.

As a general recommendation to avoid potential sensitive application data leakage, logging statements should be removed from production releases unless deemed necessary to the application or explicitly identified as safe, e.g. as a result of a security audit.

MASVS-STORAGE-2 / MSTG-STORAGE-3 / May 08, 2023
## Static Analysis

Applications will often use the Log Class and Logger Class to create logs. To discover this, you should audit the application's source code for any such logging classes. These can often be found by searching for the following keywords:

- Functions and classes, such as:
  - android.util.Log
  - Log.d | Log.e | Log.i | Log.v | Log.w | Log.wtf
  - Logger

- Keywords and system output:
  - System.out.print | System.err.print
  - logfile
  - logging
  - logs

While preparing the production release, you can use tools like ProGuard (included in Android Studio). To determine whether all logging functions from the android.util.Log class have been removed, check the ProGuard configuration file (proguard-rules.pro) for the following options (according to this example of removing logging code and this article about enabling ProGuard in an Android Studio project):
```
-assumenosideeffects class android.util.Log
{
  public static boolean isLoggable(java.lang.String, int);
  public static int v(...);
  public static int i(...);
  public static int w(...);
  public static int d(...);
  public static int e(...);
  public static int wtf(...);
}
```
Note that the example above only ensures that calls to the Log class' methods will be removed. If the string that will be logged is dynamically constructed, the code that constructs the string may remain in the bytecode. For example, the following code issues an implicit StringBuilder to construct the log statement:

Example in Java:
```
Log.v("Private key tag", "Private key [byte format]: " + key);
```
Example in Kotlin:
```
Log.v("Private key tag", "Private key [byte format]: $key")
```
The compiled bytecode, however, is equivalent to the bytecode of the following log statement, which constructs the string explicitly:
Example in Java:
```
Log.v("Private key tag", new StringBuilder("Private key [byte format]: ").append(key.toString()).toString());
```
Example in Kotlin:
```
Log.v("Private key tag", StringBuilder("Private key [byte format]: ").append(key).toString())
```
ProGuard guarantees removal of the Log.v method call. Whether the rest of the code (new StringBuilder ...) will be removed depends on the complexity of the code and the ProGuard version .

This is a security risk because the (unused) string leaks plain text data into memory, which can be accessed via a debugger or memory dumping.

Unfortunately, no silver bullet exists for this issue, but one option would be to implement a custom logging facility that takes simple arguments and constructs the log statements internally.
```
SecureLog.v("Private key [byte format]: ", key);
```

Then configure ProGuard to strip its calls.

