# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Serafim\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-dontwarn java.**
#-dontwarn sun.**
#-dontwarn dalvik.**
#-dontwarn libcore.**
#-dontwarn java.nio.file.**
#-dontwarn java.lang.invoke.**

-keepattributes Signature
-keepattributes *Annotation*

-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }

-dontwarn java.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
-dontwarn com.google.android.gms.**
-dontwarn com.android.support.**
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.retrofit2.**
-dontwarn com.google.maps.android.**
-dontwarn com.ibm.**
-dontwarn commons-lang.**

-keep class com.ibm.** { *; }

 #   compile 'com.ibm.icu:icu4j:4.8.1.1'
  #  compile 'commons-lang:commons-lang:2.6'



#-keep class com.serafimdmitrievstudio.yourway.RouteResponse { *; }
#-keep class com.serafimdmitrievstudio.yourway.ServerGetMapResponse { *; }
#-keep class com.serafimdmitrievstudio.yourway.ServerSimpleResponse { *; }
#-keep class com.serafimdmitrievstudio.yourway.ServerUserIdResponse { *; }

#-keep public class com.serafimdmitrievstudio.yourway.RouteResponse
#-keep class com.serafimdmitrievstudio.yourway.ServerGetMapResponse
#-keep class com.serafimdmitrievstudio.yourway.ServerSimpleResponse
#-keep class com.serafimdmitrievstudio.yourway.ServerUserIdResponse