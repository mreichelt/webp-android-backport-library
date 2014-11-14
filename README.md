webp-android-backport-library
=============================

Backport of WebP library for Android devices &lt; 4.0

A small project I have been working on in private - making the [official WebP library](https://chromium.googlesource.com/webm/libwebp/) easily accessible for Android developers that have to support older devices.
Basic usage:
```java
byte[] encodedWebPData = loadYourWebPDataFromSomewhere();
Bitmap bitmap = WebPBackport.decode(encodedWebPData);
```
See [MainActivity](https://github.com/mreichelt/webp-android-backport-library/blob/master/testapp/src/main/java/de/marcreichelt/webp_backport/testapp/MainActivity.java#L74) as another example.
The library automatically uses the faster native bitmap decoding feature of the system if it is available, so you don't have to care about it.

Let me know if this library is useful for you!
If enough people like it I will add it to a Maven repository so you can add it to your Android project with one line in your build.gradle. :-)


License: Same as libwebp - see [PATENTS](https://github.com/mreichelt/webp-android-backport-library/blob/master/webp_backport/jni/PATENTS) file.
