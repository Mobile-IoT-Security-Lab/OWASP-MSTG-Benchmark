# [MASTG-TEST-0035: Testing for Overlay Attacks](https://mas.owasp.org/MASTG/tests/android/MASVS-PLATFORM/MASTG-TEST-0035)
## Overview
To test for overlay attacks you need to check the app for usage of certain APIs and attributed typically used to protect against overlay attacks as well as check the Android version that app is targeting.

To mitigate these attacks please carefully read the general guidelines about Android View security in the [Android Developer Documentation ↗](https://developer.android.com/reference/android/view/View#security). For instance, the so-called touch filtering is a common defense against tapjacking, which contributes to safeguarding users against these vulnerabilities, usually in combination with other techniques and considerations as we introduce in this section.
MASVS-PLATFORM-3 / MSTG-PLATFORM-9 / May 08, 2023
## Static Analysis
To start your static analysis you can check the app for the following methods and attributes (non-exhaustive list):

- Override `onFilterTouchEventForSecurity`  for more fine-grained control and to implement a custom security policy for views.
- Set the layout attribute `android:filterTouchesWhenObscured`  to true or call `setFilterTouchesWhenObscured`.
- Check `FLAG_WINDOW_IS_OBSCURED `(since API level 9) or `FLAG_WINDOW_IS_PARTIALLY_OBSCURED `(starting on API level 29).

Some attributes might affect the app as a whole, while others can be applied to specific components. The latter would be the case when, for example, there is a business need to specifically allow overlays while wanting to protect sensitive input UI elements. The developers might also take additional precautions to confirm the user's actual intent which might be legitimate and tell it apart from a potential attack.

As a final note, always remember to properly check the API level that app is targeting and the implications that this has. For instance, Android 8.0 (API level 26) introduced changes to apps requiring `SYSTEM_ALERT_WINDOW` ("draw on top"). From this API level on, apps using `TYPE_APPLICATION_OVERLAY` will be always shown above other windows  having other types such as `TYPE_SYSTEM_OVERLAY` or `TYPE_SYSTEM_ALERT`. You can use this information to ensure that no overlay attacks may occur at least for this app in this concrete Android version.
