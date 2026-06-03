# HazelGym demo/release build.
# Keep API contracts stable while R8 shrinks the rest of the APK.
-keepattributes Signature, InnerClasses, EnclosingMethod, RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations, AnnotationDefault

-keep class com.hazelgym.mobile.data.model.** { *; }
-keep interface com.hazelgym.mobile.data.remote.** { *; }

-dontwarn javax.annotation.**
-dontwarn org.codehaus.mojo.animal_sniffer.**
