# [MASTG-TEST-0025: Testing for Injection Flaws](https://mas.owasp.org/MASTG/tests/android/MASVS-CODE/MASTG-TEST-0025)

## Overview

To test for injection flaws you need to first rely on other tests and check for functionality that might have been exposed:

- [Testing Deep Links](#)
- [Testing for Sensitive Functionality Exposure Through IPC](#)
- [Testing for Overlay Attacks](#)
MASVS-CODE-4 / MSTG-PLATFORM-2 / April 27, 2024
## Static Analysis

An example of a vulnerable IPC mechanism is shown below.

You can use ContentProviders to access database information, and you can probe services to see if they return data. If data is not validated properly, the content provider may be prone to SQL injection while other apps are interacting with it. See the following vulnerable implementation of a ContentProvider:

<provider
  android:name="OVH.TO.CODING.083.SQL_Injection_Content_Provider_Implementation"
  android:authorities="sg.vp.owasp_mobile.provider.College"/>

The AndroidManifest.xml above defines a content provider that's exported and therefore available to all other apps. The query function in the OVH.TO.CODING.083.SQL_Injection_Content_Provider_Implementation.java class should be inspected.

@Override
public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
  SQLiQueryBuilder db = new SQLiQueryBuilder();
  db.setTables(STUDENTS_TABLE_NAME);

  switch (uriMatcher.match(uri)) {
    case STUDENTS:
      db.setProjectionMap(STUDENTS_PROJECTION_MAP);
      break;

    case STUDENT_ID:
      // SQL Injection when providing an ID
      appendWhere("_ID = " + uri.getPathSegments().get(1));
      db.appendWhere("_ID = " + uri.getPathSegments().get(1).toString());
      break;

    default:
      throw new IllegalArgumentException("Unknown URI: " + uri);
  }

  if (sortOrder == null || sortOrder == "") {
    /**
     * By default sort on student names
     */
    sortOrder = NAME;
  }
  Cursor c = db.query(db, projection, selection, selectionArgs,null, null, sortOrder);

  /**
   * register to watch a content URI for changes
   */
  c.setNotificationUri(getContext().getContentResolver(), uri);
  return c;
}

While the user is providing a STUDENT_ID at content://sg.vp.owasp_mobile.provider.College/students, the query statement is prone to SQL injection. Obviously, [prepared statements](#) must be used to avoid SQL injection, but input validation should also be applied so that only input that the app is expecting is processed.

All app functions that process data coming in through the UI should implement input validation:

- For user interface input, [Android Saripaar v2](#) can be used.
- For input from IPC or URL schemes, a validation function should be created. For example, the following determines whether the string is alphanumeric:

public boolean isAlphaNumeric(String s){
  String pattern= "^[a-zA-Z0-9]*$";
  return s.matches(pattern);
}

An alternative to validation functions is type conversion, with, for example, Integer.parseInt if only integers are expected. The [OWASP Input Validation Cheat Sheet](#) contains more information about this topic.