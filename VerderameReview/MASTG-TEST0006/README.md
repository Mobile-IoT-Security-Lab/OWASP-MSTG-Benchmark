# [MASTG-TEST-0006: Determining Whether the Keyboard Cache Is Disabled for Text Input Fields](https://mas.owasp.org/MASTG/tests/android/MASVS-STORAGE/MASTG-TEST-0006)

## Nota

- rivedere insieme implementazione


## Implementation

- Creato Fake Login ovvero qualsiasi username e pwd vanno bene, non vengono fatti controlli sulle credenziali

Sebastian **non suggerisce** l’aggiunta all’EditText di un inputType

```java
 <EditText
        android:id="@+id/KeyBoardCache"
        android:inputType="textNoSuggestions" />
```

The code for all input fields that take sensitive information should include this XML attribute to [disable the keyboard suggestions ↗](https://developer.android.com/reference/android/text/InputType.html#TYPE_TEXT_FLAG_NO_SUGGESTIONS).

## Overview
MASVS-STORAGE-2 / MSTG-STORAGE-5 / May 08, 2023
## Static Analysis
In the layout definition of an activity, you can define TextViews that have XML attributes. If the XML attribute android:inputType is given the value textNoSuggestions, the keyboard cache will not be shown when the input field is selected. The user will have to type everything manually.
```
   <EditText
        android:id="@+id/KeyBoardCache"
        android:inputType="textNoSuggestions" />

```
The code for all input fields that take sensitive information should include this XML attribute to disable the keyboard suggestions .

Alternatively, the developer can use the following constants:

Check the application code to verify that none of the input types are being overwritten. For example, by doing findViewById(R.id.KeyBoardCache).setInputType(InputType.TYPE_CLASS_TEXT) the input type of the input field KeyBoardCache is set to text reenabling the keyboard cache.

| XML android:inputType      | Code InputType                      | API level |
|------------------------------|---------------------------------------|-----------|
| [textPassword](https://developer.android.com/reference/android/text/InputType.html#TYPE_TEXT_VARIATION_PASSWORD)      | TYPE_TEXT_VARIATION_PASSWORD      | 3         |
| [textVisiblePassword](https://developer.android.com/reference/android/text/InputType.html#TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | 3         |
| [numberPassword](https://developer.android.com/reference/android/text/InputType.html#TYPE_NUMBER_VARIATION_PASSWORD)    | TYPE_NUMBER_VARIATION_PASSWORD    | 11        |
| [textWebPassword](https://developer.android.com/reference/android/text/InputType.html#TYPE_TEXT_VARIATION_WEB_PASSWORD)   | TYPE_TEXT_VARIATION_WEB_PASSWORD   | 11        |

Finally, check the minimum required SDK version in the Android Manifest (android:minSdkVersion) since it must support the used constants (for example, Android SDK version 11 is required for textWebPassword). Otherwise, the compiled app would not honor the used input type constants allowing keyboard caching.