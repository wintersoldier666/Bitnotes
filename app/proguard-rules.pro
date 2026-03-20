# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/android-sdk/tools/proguard/proguard-android.txt

# Keep cryptographic classes
-keep class javax.crypto.** { *; }
-keep class java.security.** { *; }

# Keep Room entities
-keep class com.bitnotes.app.data.model.** { *; }
-keepclassmembers class com.bitnotes.app.data.model.** { *; }

# Keep Hilt
-keep class dagger.hilt.** { *; }
-keepclassmembers class * {
    @dagger.hilt.android.lifecycle.HiltViewModel <init>(...);
}

# Keep Gson
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep backup data classes
-keep class com.bitnotes.app.data.model.BackupData { *; }
-keep class com.bitnotes.app.data.model.NoteBackup { *; }

# Prevent reverse engineering of encryption
-repackageclasses 'o'
-allowaccessmodification
-optimizationpasses 5
-dontusemixedcaseclassnames

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
}
