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
-keep class kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck
-keep class kotlin.internal.annotations.AvoidUninitializedObjectCopyingCheck
# Lambda
-dontwarn java.lang.invoke.*

-keep class javax.annotation.* { *; }
-keepattributes Deprecated
-keepattributes InnerClasses
-keepattributes JavascriptInterface

-optimizations !code/simplification/cast,!field/*,!class/merging/*
-allowaccessmodification
-optimizationpasses 5


#-keep class io.reactivex.**

#Standard android:
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keep public class android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-keep public class com.android.vending.licensing.ILicensingService

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

## Square Picasso specific rules ##
## https://square.github.io/picasso/ ##

-dontwarn com.squareup.okhttp.**


#Mockito
-dontwarn org.mockito.**

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn com.google.errorprone.annotations.*

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep public class javax.annotation.* { public *; }
-dontwarn javax.annotation.**

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Gson
-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Appcompat and support

-keep class com.example.BuildConfig { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontwarn android.app.Notification

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }


## Joda Time 2.3
-keep class org.joda.convert.** { *; }
-keep interface org.joda.convert.** { *; }

-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

#android
-dontwarn android.**

## joda-time-android 2.8.0
# This is only necessary if you are not including the optional joda-convert dependency

-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# Dagger ProGuard rules.
# https://github.com/square/dagger

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection




-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-dontwarn ch.qos.logback.core.net.*


-dontwarn com.google.errorprone.annotations.**
-keep class com.google.errorprone.annotations.** {*;}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-keep class com.google.j2objc.annotations.** { *; }
-dontwarn   com.google.j2objc.annotations.**
-keep class java.lang.ClassValue { *; }
-dontwarn   java.lang.ClassValue
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }
-dontwarn   org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.concurrent.LazyInit
-dontwarn com.google.errorprone.annotations.ForOverride

#Firebase
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keep class com.google.android.gms.iid.zzd { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

# Only necessary if you downloaded the SDK jar directly instead of from maven.
-keep class com.shaded.fasterxml.jackson.** { *; }

# Crashlytics configuration
-keepattributes SourceFile, LineNumberTable, *Annotation*
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep public class * extends java.lang.Exception


-keep class com.google.analytics.** { *; }


#Data binding:
-dontwarn android.databinding.**
-keep class android.databinding.annotationprocessor.** { *; }
-keep class android.databinding.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepattributes *Annotation*
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*
-keep class android.support.v4.content.ContextCompat { *; }

-keep class com.adobe.** { *; }
-keep class fi.iki.** { *; }
-keep class org.readium.** { *; }
-keep interface org.readium.** { *; }

# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/alcy/Library/Android/sdk/tools/proguard/proguard-android.txt
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

# Lambda
-dontwarn java.lang.invoke.*

-keep class javax.annotation.* { *; }
-keepattributes Deprecated
-keepattributes InnerClasses
-keepattributes JavascriptInterface

-optimizations !code/simplification/cast,!field/*,!class/merging/*
-allowaccessmodification
-optimizationpasses 5

-dontpreverify
-keep class com.bookbeat.android.audio.model.consumption.** { *; }
-keep class com.bookbeat.android.api.model.** { *; }
-keep class com.bookbeat.android.api.hal.** { *; }
-keep class com.bookbeat.android.domain.** { *; }
-keep class com.bookbeat.android.history.** { *; }
-keep class com.bookbeat.android.history.** { *; }
-keep class  com.bookbeat.android.tracking.BookBeatTrackingDTO { *; }

#-keep class io.reactivex.**

#Standard android:
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

-keepattributes *Annotation*
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

-keep public class android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}
-keep public class * extends android.support.v7.preference.Preference {
  public <init>(android.content.Context, android.util.AttributeSet);
}

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclassmembers class * extends android.content.Context {
    public void *(android.view.View);
    public void *(android.view.MenuItem);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class **.R$* {
    public static <fields>;
}

-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}

# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

-keep public class com.android.vending.licensing.ILicensingService

# Retrofit 1.X

-keep class com.squareup.okhttp.** { *; }
-keep class retrofit.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn com.squareup.okhttp.**
-dontwarn okio.**
-dontwarn retrofit.**
-dontwarn rx.**

-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

# If in your rest service interface you use methods with Callback argument.
-keepattributes Exceptions

# If your rest service methods throw custom exceptions, because you've defined an ErrorHandler.
-keepattributes Signature

# Also you must note that if you are using GSON for conversion from JSON to POJO representation, you must ignore those POJO classes from being obfuscated.
# Here include the POJO's that have you have created for mapping JSON response to POJO for example.

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

## Square Picasso specific rules ##
## https://square.github.io/picasso/ ##

-dontwarn com.squareup.okhttp.**


#Leakcanary
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }
-dontwarn android.app.Notification

#Mockito
-dontwarn org.mockito.**

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-dontwarn com.google.errorprone.annotations.*

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**
-keep public class javax.annotation.* { public *; }
-dontwarn javax.annotation.**

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
    public static final *** NULL;
}

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclassmembernames class * {
    @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

# Gson
-keepattributes EnclosingMethod

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Sdk
-keep public interface com.zendesk.** { *; }
-keep public class com.zendesk.** { *; }
-dontwarn java.awt.**

# Appcompat and support

-keep class com.example.BuildConfig { *; }
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
-dontwarn android.app.Notification

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep class android.support.v7.widget.RoundRectDrawable { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

#sqlite:
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }

-dontwarn rx.**
-dontwarn com.google.appengine.api.urlfetch.**

-keepattributes *Annotation*

## Joda Time 2.3
-keep class org.joda.convert.** { *; }
-keep interface org.joda.convert.** { *; }

-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

-dontwarn org.joda.convert.**
-dontwarn org.joda.time.**
-keep class org.joda.time.** { *; }
-keep interface org.joda.time.** { *; }

#android
-dontwarn android.**

## joda-time-android 2.8.0
# This is only necessary if you are not including the optional joda-convert dependency

-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# Dagger ProGuard rules.
# https://github.com/square/dagger

-dontwarn dagger.internal.codegen.**
-keepclassmembers,allowobfuscation class * {
    @javax.inject.* *;
    @dagger.* *;
    <init>();
}

-keep class dagger.* { *; }
-keep class javax.inject.* { *; }
-keep class * extends dagger.internal.Binding
-keep class * extends dagger.internal.ModuleAdapter
-keep class * extends dagger.internal.StaticInjection

# Configuration for Guava 18.0
#
# disagrees with instructions provided by Guava project: https://code.google.com/p/guava-libraries/wiki/UsingProGuardWithGuava


-keep class com.google.common.io.Resources {
    public static <methods>;
}
-keep class com.google.common.collect.Lists {
    public static ** reverse(**);
}
-keep class com.google.common.base.Charsets {
    public static <fields>;
}

-keep class com.google.common.base.Joiner {
    public static com.google.common.base.Joiner on(java.lang.String);
    public ** join(...);
}

-keep class com.google.common.collect.MapMakerInternalMap$ReferenceEntry
-keep class com.google.common.cache.LocalCache$ReferenceEntry

# http://stackoverflow.com/questions/9120338/proguard-configuration-for-guava-with-obfuscation-and-optimization
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe

-dontwarn java.nio.file.*

# Guava 19.0
-dontwarn java.lang.ClassValue
-dontwarn com.google.j2objc.annotations.Weak
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# OrmLite uses reflection
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }

-keepclassmembers class * {
  public <init>(android.content.Context);
}

# Keep all model classes that are used by OrmLite
# Also keep their field names and the constructor
-keep @com.j256.ormlite.table.DatabaseTable class * {
    @com.j256.ormlite.field.DatabaseField <fields>;
    @com.j256.ormlite.field.ForeignCollectionField <fields>;
    <init>();
}


 -dontnote com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
 -keepclassmembers class * extends com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper {
     <init>(android.content.Context);
 }

 -dontnote com.j256.ormlite.field.DatabaseFieldConfig
 -keepclassmembers class com.j256.ormlite.field.DatabaseFieldConfig {
     <fields>;
 }

 -dontnote com.j256.ormlite.dao.Dao
 -keepclassmembers class * implements com.j256.ormlite.dao.Dao {
     <init>(**);
     <init>(**, java.lang.Class);
 }

 -dontnote com.j256.ormlite.android.AndroidLog
 -keep class com.j256.ormlite.android.AndroidLog {
     <init>(java.lang.String);
 }

 -dontnote com.j256.ormlite.table.DatabaseTable
 -keep @com.j256.ormlite.table.DatabaseTable class * {
     void set*(***);
     *** get*();
 }

 -dontnote com.j256.ormlite.field.DatabaseField
 -keepclassmembers @interface com.j256.ormlite.field.DatabaseField {
     <methods>;
 }

 -dontnote com.j256.ormlite.field.ForeignCollectionField
 -keepclassmembers @interface com.j256.ormlite.field.ForeignCollectionField {
     <methods>;
 }

-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-dontwarn ch.qos.logback.core.net.*


-dontwarn com.google.errorprone.annotations.**
-keep class com.google.errorprone.annotations.** {*;}

-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn sun.misc.Unsafe
-keep class com.google.j2objc.annotations.** { *; }
-dontwarn   com.google.j2objc.annotations.**
-keep class java.lang.ClassValue { *; }
-dontwarn   java.lang.ClassValue
-keep class org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement { *; }
-dontwarn   org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn com.google.errorprone.annotations.CanIgnoreReturnValue
-dontwarn com.google.errorprone.annotations.concurrent.LazyInit
-dontwarn com.google.errorprone.annotations.ForOverride

#Firebase, included by something???
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keep class com.google.android.gms.iid.zzd { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.w3c.dom.**
-dontwarn org.joda.time.**
-dontwarn org.shaded.apache.**
-dontwarn org.ietf.jgss.**

# Only necessary if you downloaded the SDK jar directly instead of from maven.
-keep class com.shaded.fasterxml.jackson.** { *; }

# Crashlytics configuration
-keepattributes SourceFile, LineNumberTable, *Annotation*
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
-keep public class * extends java.lang.Exception


-keep class com.google.analytics.** { *; }


#Data binding:
-dontwarn android.databinding.**
-keep class android.databinding.annotationprocessor.** { *; }
-keep class android.databinding.** { *; }
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keepattributes *Annotation*
-keepattributes javax.xml.bind.annotation.*
-keepattributes javax.annotation.processing.*
-keep class android.support.v4.content.ContextCompat { *; }

-keep class com.adobe.** { *; }
-keep class fi.iki.** { *; }
-keep class org.readium.** { *; }
-keep interface org.readium.** { *; }


#Adobe reader javascript interfaces:
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPaginationChanged
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onSettingsApplied
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onExternalLinkClicked
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onGenericHostCallback
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onReaderInitialized
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onContentLoaded
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPageLoaded
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onIsMediaOverlayAvailable
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onMediaOverlayStatusChanged
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$osetJSRetVal
-keep public class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$getBookmarkData

-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPaginationChanged
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onSettingsApplied
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onExternalLinkClicked
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onGenericHostCallback
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onReaderInitialized
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onContentLoaded
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPageLoaded
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onIsMediaOverlayAvailable
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onMediaOverlayStatusChanged
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$osetJSRetVal
-keep public class * implements com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$getBookmarkData

-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPaginationChanged {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onSettingsApplied {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onExternalLinkClicked {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onGenericHostCallback {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onReaderInitialized {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onContentLoaded {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onPageLoaded {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onIsMediaOverlayAvailable {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$onMediaOverlayStatusChanged {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$osetJSRetVal {
    <methods>;
}
-keepclassmembers class com.adobe.rmsdk.android.RDMWebViewController.EpubInterface$getBookmarkData {
    <methods>;
}

-keep class com.datalogics.** { *; }
-dontwarn com.datalogics.**
-keep class pl.polidea.** { *; }
-dontwarn pl.polidea.**
-keep class se.bonnierforlagen.** { *; }
-dontwarn se.bonnierforlagen.**

-dontwarn com.google.android.gms.crash.internal.service.**

-dontwarn org.apache.log4j.**
-dontwarn org.apache.commons.logging.**

#logback
-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*
-dontwarn ch.qos.logback.core.net.*

-dontwarn de.psdev.**

-keep class com.facebook.stetho.** { *; }
-dontwarn com.facebook.stetho.**




