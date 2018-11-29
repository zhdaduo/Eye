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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# 指定代码的压缩级别
-optimizationpasses 5
# 是否使用大小写混合
-dontusemixedcaseclassnames
# 是否混淆第三方jar
-dontskipnonpubliclibraryclasses
# 混淆时是否做预校验
-dontpreverify
# 混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

##########
# Maintain all attributes - To avoid having to add each in several different places below
##########
-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,LocalVariable*Table,Synthetic

##########
# Android
##########
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep class android.support.** {*;}
-keep class com.google.**{*;}

-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
 -keepclasseswithmembernames class * {
     native <methods>;
 }
 -keepclassmembers class * extends android.app.Activity{
     public void *(android.view.View);
 }
 -keep public class * extends android.view.View{
     *** get*();
     void set*(***);
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keepclasseswithmembers class * {
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
 }
 -keep class **.R$* {
  *;
 }
 -keepclassmembers class * {
     void *(**On*Event);
 }

# Data Binding
#-dontwarn android.databinding.**
#-keep class android.databinding.** { *; }

#-keepclassmembers class * extends android.content.Context {
#   public void *(android.view.View);
#   public void *(android.view.MenuItem);
#}

##########
# data class
##########

-keep class com.mor.eye.repository.data.** { *; }

##########
# View - Gets and setters - keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
##########

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

##########
#Enums - For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
##########

#-keepclassmembers enum * {
#     public static **[] values();
#     public static ** valueOf(java.lang.String);
# }

##########
# Parcelables: Mantain the parcelables working
##########

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

#############
# Serializables
#############

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

##########
# Kotlin
##########

-dontwarn kotlin.**
-dontnote kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}

#Ignore null checks at runtime
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

#############
# WebViews
#############
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keep class android.support.v8.renderscript.** { *; }
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
     public boolean *(android.webkit.WebView, java.lang.String);
 }
-keepclassmembers class * extends android.webkit.WebViewClient {
     public void *(android.webkit.WebView, jav.lang.String);
 }

##########
# Android architecture components: Lifecycle ( https://issuetracker.google.com/issues/62113696 )
##########
# LifecycleObserver's empty constructor is considered to be unused by proguard
#-keepclassmembers class * implements android.arch.lifecycle.LifecycleObserver {
#    <init>(...);
#}
# ViewModel's empty constructor is considered to be unused by proguard
#-keepclassmembers class * extends android.arch.lifecycle.ViewModel {
#    <init>(...);
#}
# keep Lifecycle State and Event enums values
#-keepclassmembers class android.arch.lifecycle.Lifecycle$State { *; }
#-keepclassmembers class android.arch.lifecycle.Lifecycle$Event { *; }
# keep methods annotated with @OnLifecycleEvent even if they seem to be unused
# (Mostly for LiveData.LifecycleBoundObserver.onStateChange(), but who knows)
#-keepclassmembers class * {
#    @android.arch.lifecycle.OnLifecycleEvent *;
#}

#############
# Okhttp3
#############

-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.** { *; }
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *; }

#############
# Okio
#############

-dontwarn okio.**

#############
# Retrofit
#############

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class com.mor.eye.repository.EyeDataSource { *; }

##########
# Glide
##########

-dontwarn com.bumptech.glide.**
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

##########
# RxJava 2
##########

-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#############
# Stetho
#############
#-dontnote com.facebook.stetho.**

##########
# Gson
##########
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }

##########
# Epoxy
##########
#-keep class * extends com.airbnb.epoxy.EpoxyController { *; }
#-keep class * extends com.airbnb.epoxy.ControllerHelper { *; }

##########
# Gsyvideoplayer
##########
-keep class com.shuyu.gsyvideoplayer.video.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.**
-keep class com.shuyu.gsyvideoplayer.video.base.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.video.base.**
-keep class com.shuyu.gsyvideoplayer.utils.** { *; }
-dontwarn com.shuyu.gsyvideoplayer.utils.**
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**