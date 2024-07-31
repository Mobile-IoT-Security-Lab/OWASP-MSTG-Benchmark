MASTG-TEST0002 Attack:
- pull from /data/data/com.example.mastg_test0002/shared_prefs/ the MyPrefs.xml file
- Modify the content of MyPrefs.xml :
  - changing the value of     <boolean name="isLoggedIn" value="false" /> in     <boolean name="isLoggedIn" value="true" />
- Save MyPrefs.xml
- Push MyPrefs.xml in /sdcard
- from /sdcard move the file to /data/data/com.example.mastg_test0002/shared_pref/
