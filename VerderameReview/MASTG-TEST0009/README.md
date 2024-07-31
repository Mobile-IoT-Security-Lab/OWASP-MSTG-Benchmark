# [MASTG-TEST-0009: Testing Backups for Sensitive Data](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0009)

## Overview
MASVS-STORAGE-2 / MSTG-STORAGE-8 / May 08, 2023
## Static Analysis

### Local

Check the `AndroidManifest.xml` file for the following flag:
```
android:allowBackup="true"
```

If the flag value is true, determine whether the app saves any kind of sensitive data (check the test case "Testing for Sensitive Data in Local Storage").

### Cloud

Regardless of whether you use key/value backup or auto backup, you must determine the following:

- Which files are sent to the cloud (e.g., SharedPreferences)
- Whether the files contain sensitive information
- Whether sensitive information is encrypted before being sent to the cloud.

If you don’t want to share files with Google Cloud, you can exclude them from [Auto Backup](https://developer.android.com/guide/topics/data/autobackup). Sensitive information stored at rest on the device should be encrypted before being sent to the cloud.

#### Auto Backup

You configure Auto Backup via the boolean attribute `android:allowBackup` within the application's manifest file. [Auto Backup](https://developer.android.com/guide/topics/data/autobackup) is enabled by default for applications that target Android 6.0 (API level 23). You can use the attribute `android:fullBackupOnly` to activate auto backup when implementing a backup agent, but this attribute is available for Android versions 6.0 and above only. Other Android versions use key/value backup instead.
```
android:fullBackupOnly
```

Auto backup includes almost all the app files and stores up to 25 MB of them per app in the user’s Google Drive account. Only the most recent backup is stored; the previous backup is deleted.

#### Key/Value Backup

To enable key/value backup, you must define the backup agent in the manifest file. Look in `AndroidManifest.xml` for the following attribute:
```
android:backupAgent
```

To implement key/value backup, extend one of the following classes:

- [BackupAgent](https://developer.android.com/reference/android/app/backup/BackupAgent)
- [BackupAgentHelper](https://developer.android.com/reference/android/app/backup/BackupAgentHelper)

To check for key/value backup implementations, look for these classes in the source code.

