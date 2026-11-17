# Keep rules for the release build.

-dontobfuscate

-keep class androidx.** { *; }
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application

# The Tips screen loads its provider by the class name in assets/features.txt.
-keep class dev.kisumu.market.tips.MarketTips { <init>(); }
