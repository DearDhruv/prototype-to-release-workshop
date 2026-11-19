# Navigation 3 and kotlinx.serialization keep rules for com.deardhruv.ptr
# Keep NavKey implementations and their serializable companions / generated serializers
-keep @kotlinx.serialization.Serializable class com.deardhruv.ptr.** { *; }
-keepclassmembers class com.deardhruv.ptr.** {
    *** Companion;
    *** $serializer;
}
-keepclasseswithmembers class com.deardhruv.ptr.** implements androidx.navigation3.runtime.NavKey { *; }

# Kotlinx Serialization general rules
-keepattributes *Annotation*, InnerClasses, EnclosingMethod
-keepclassmembers class * {
    @kotlinx.serialization.SerialName <fields>;
}
