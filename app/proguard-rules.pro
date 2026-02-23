# ProGuard rules for Facial Emotion Recognition app

# ── TensorFlow Lite ──────────────────────────────────────
-keep class org.tensorflow.lite.** { *; }
-keep class org.tensorflow.lite.gpu.** { *; }
-dontwarn org.tensorflow.lite.**

# ── MediaPipe ────────────────────────────────────────────
-keep class com.google.mediapipe.** { *; }
-dontwarn com.google.mediapipe.**

# ── Hilt / Dagger ─────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.AndroidEntryPoint class *
-keep class **_Factory { *; }
-keep class **_HiltComponents* { *; }
-dontwarn dagger.hilt.**

# ── CameraX ──────────────────────────────────────────────
-keep class androidx.camera.** { *; }
-dontwarn androidx.camera.**

# ── Kotlin Coroutines ────────────────────────────────────
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-dontwarn kotlinx.coroutines.**

# ── App model classes ─────────────────────────────────────
-keep class com.sakhidev.fer.model.** { *; }

# ── General Android rules ──────────────────────────────────
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keepclassmembers class * { @android.webkit.JavascriptInterface <methods>; }
