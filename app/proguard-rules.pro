# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose
-dontoptimize
-dontpreverify
-ignorewarnings                      #忽略警告
-optimizationpasses 5

-keepattributes *Annotation*

#native方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

# Understand the @Keep support annotation.
-keep class androidx.annotation.Keep

-keep @androidx.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}

-keepattributes Signature #范型
-keepattributes *Annotation*
#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}
-keep interface okhttp3.**{*;}
-keep class * {
    native <methods>;
}


-keep class com.vecentek.decorelink.sdk.bean.**{*;}
-keep class com.vecentek.decorelink.sdk.bean.reauest.**{*;}
-keep class com.vecentek.decorelink.sdk.bean.keys.**{*;}
-keep class com.vecentek.decorelink.sdk.jni.**{*;}
-keep class com.vecentek.decorelink.sdk.callback.info.**{*;}


#Gson混淆配置
-keep class com.google.gson.** { *; }



-dontshrink

-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

#-keep class android.bluetooth.BluetoothManager {
#     *;
#}
#
#-keep class com.dingjikerbo.bluetooth.library.BluetoothManager$* {
#     *;
#}

-keep class com.vecentek.decorelink.ble.library.connect.response.BleResponse {
     <methods>;
}

-keep interface * extends com.vecentek.decorelink.ble.library.connect.response.BleResponse