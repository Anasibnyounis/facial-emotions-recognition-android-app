# 📱 Facial Emotion Recognition — Android App
### Complete Development Roadmap
> Developed by **SAKHI DEV**

---

## 📌 Table of Contents
1. [Project Overview](#project-overview)
2. [Tech Stack](#tech-stack)
3. [Project Architecture](#project-architecture)
4. [Folder Structure](#folder-structure)
5. [Dependencies & Gradle Setup](#dependencies--gradle-setup)
6. [Screen-by-Screen Breakdown](#screen-by-screen-breakdown)
7. [ML Model Integration](#ml-model-integration)
8. [Camera Integration (CameraX)](#camera-integration-camerax)
9. [Emotion Detection Logic](#emotion-detection-logic)
10. [Device Support (Phone, Tablet, VR)](#device-support-phone-tablet-vr)
11. [Development Phases](#development-phases)
12. [Testing Strategy](#testing-strategy)
13. [Build & Deployment](#build--deployment)

---

## Project Overview

| Field | Detail |
|---|---|
| App Name | Facial Emotion Recognition |
| Developer | SAKHI DEV |
| Platform | Android (Phone, Tablet, VR Glasses) |
| Language | Kotlin |
| Min SDK | API 26 (Android 8.0) |
| Target SDK | API 34 (Android 14) |
| IDE | Android Studio Hedgehog (2023.1.1+) |
| Architecture | MVVM (Model-View-ViewModel) |

### Features
- Real-time facial detection via front camera
- Emotion classification: Happy, Sad, Nervous, Neutral
- Eye state detection: Open / Closed (blink detection)
- Adaptive UI for Phone, Tablet, and VR Android devices
- Minimalistic, beautiful Material Design 3 interface

---

## Tech Stack

| Layer | Technology | Purpose |
|---|---|---|
| Language | **Kotlin** | Primary development language |
| UI Framework | **Jetpack Compose** | Declarative modern UI |
| Design System | **Material Design 3** | Beautiful, consistent UI components |
| Camera | **CameraX** | Real-time camera preview + frame capture |
| Face Detection | **MediaPipe Face Landmarker** | 468 facial landmarks, eye tracking |
| Emotion AI | **TensorFlow Lite (FER+ Model)** | On-device emotion classification |
| Architecture | **MVVM + LiveData/StateFlow** | Clean, testable code structure |
| DI | **Hilt (Dagger)** | Dependency injection |
| Navigation | **Jetpack Navigation Component** | Screen-to-screen navigation |
| Splash Screen | **Android 12+ Splash Screen API** | Native animated splash screen |
| Concurrency | **Kotlin Coroutines + Flow** | Async image processing |
| Adaptive Layout | **WindowSizeClass** | Phone/Tablet/VR responsive UI |
| Build System | **Gradle (Kotlin DSL)** | Build configuration |
| Version Control | **Git** | Source control |

---

## Project Architecture

The app follows **MVVM (Model-View-ViewModel)** with a clean separation of concerns:

```
UI Layer (Compose Screens)
        ↕
ViewModel Layer (StateFlow / LiveData)
        ↕
Repository Layer (Data sources)
        ↕
ML Layer (TFLite + MediaPipe)
        ↕
Camera Layer (CameraX)
```

### Architecture Diagram

```
┌─────────────────────────────────────────────────┐
│                  UI LAYER                        │
│  SplashScreen → LandingScreen → CameraScreen    │
└────────────────────┬────────────────────────────┘
                     │ observes state
┌────────────────────▼────────────────────────────┐
│               VIEWMODEL LAYER                    │
│  CameraViewModel  │  EmotionViewModel            │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│              REPOSITORY LAYER                    │
│  EmotionRepository  │  FaceDetectionRepository   │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│                 ML LAYER                         │
│  TFLiteEmotionClassifier │ MediaPipeFaceLandmark  │
└────────────────────┬────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────┐
│              CAMERA LAYER                        │
│           CameraX (ImageAnalysis)                │
└─────────────────────────────────────────────────┘
```

---

## Folder Structure

```
FacialEmotionRecognition/
│
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/sakhidev/fer/
│   │   │   │   │
│   │   │   │   ├── MainActivity.kt               ← Entry point
│   │   │   │   │
│   │   │   │   ├── ui/
│   │   │   │   │   ├── splash/
│   │   │   │   │   │   └── SplashScreen.kt       ← Splash UI
│   │   │   │   │   ├── landing/
│   │   │   │   │   │   └── LandingScreen.kt      ← Landing/Home UI
│   │   │   │   │   ├── camera/
│   │   │   │   │   │   ├── CameraScreen.kt       ← Camera + Results UI
│   │   │   │   │   │   └── CameraPreview.kt      ← CameraX Composable
│   │   │   │   │   ├── components/
│   │   │   │   │   │   ├── EmotionCard.kt        ← Emotion result card
│   │   │   │   │   │   ├── FaceOverlay.kt        ← Face bounding box overlay
│   │   │   │   │   │   └── EmotionBadge.kt       ← Emotion label badge
│   │   │   │   │   └── theme/
│   │   │   │   │       ├── Theme.kt              ← Material3 theme
│   │   │   │   │       ├── Color.kt              ← Color palette
│   │   │   │   │       └── Type.kt               ← Typography
│   │   │   │   │
│   │   │   │   ├── viewmodel/
│   │   │   │   │   ├── CameraViewModel.kt        ← Camera state management
│   │   │   │   │   └── EmotionViewModel.kt       ← Emotion results state
│   │   │   │   │
│   │   │   │   ├── repository/
│   │   │   │   │   ├── EmotionRepository.kt      ← Emotion data source
│   │   │   │   │   └── FaceRepository.kt         ← Face detection data source
│   │   │   │   │
│   │   │   │   ├── ml/
│   │   │   │   │   ├── TFLiteEmotionClassifier.kt ← TensorFlow Lite wrapper
│   │   │   │   │   ├── MediaPipeFaceDetector.kt  ← MediaPipe wrapper
│   │   │   │   │   └── EyeStateDetector.kt       ← Eye open/close logic
│   │   │   │   │
│   │   │   │   ├── camera/
│   │   │   │   │   ├── CameraManager.kt          ← CameraX setup
│   │   │   │   │   └── FrameAnalyzer.kt          ← ImageAnalysis use case
│   │   │   │   │
│   │   │   │   ├── model/
│   │   │   │   │   ├── EmotionResult.kt          ← Data class for emotion output
│   │   │   │   │   ├── FaceData.kt               ← Data class for face info
│   │   │   │   │   └── EyeState.kt               ← Enum: OPEN / CLOSED
│   │   │   │   │
│   │   │   │   ├── navigation/
│   │   │   │   │   └── AppNavigation.kt          ← NavGraph definition
│   │   │   │   │
│   │   │   │   └── di/
│   │   │   │       └── AppModule.kt              ← Hilt DI module
│   │   │   │
│   │   │   ├── assets/
│   │   │   │   ├── fer_model.tflite              ← TFLite emotion model
│   │   │   │   └── face_landmarker.task          ← MediaPipe model
│   │   │   │
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   │   └── ic_logo.xml               ← App logo (vector)
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml
│   │   │   │   │   └── themes.xml
│   │   │   │   └── mipmap/                       ← App icons
│   │   │   │
│   │   │   └── AndroidManifest.xml
│   │   │
│   │   └── test/ & androidTest/                  ← Unit and UI tests
│   │
│   └── build.gradle.kts                          ← App-level Gradle
│
├── build.gradle.kts                              ← Project-level Gradle
├── settings.gradle.kts
├── gradle.properties
└── README.md
```

---

## Dependencies & Gradle Setup

### `build.gradle.kts` (Project Level)

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

### `build.gradle.kts` (App Level)

```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.sakhidev.fer"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sakhidev.fer"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
}

dependencies {
    // ── Jetpack Compose ──────────────────────────────────────
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // ── Navigation ───────────────────────────────────────────
    implementation("androidx.navigation:navigation-compose:2.7.6")

    // ── Lifecycle / ViewModel ────────────────────────────────
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // ── CameraX ──────────────────────────────────────────────
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("androidx.camera:camera-extensions:1.3.1")

    // ── TensorFlow Lite ──────────────────────────────────────
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-support:0.4.4")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0")

    // ── MediaPipe ────────────────────────────────────────────
    implementation("com.google.mediapipe:tasks-vision:0.10.10")

    // ── Hilt (Dependency Injection) ──────────────────────────
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    // ── Splash Screen ────────────────────────────────────────
    implementation("androidx.core:core-splashscreen:1.0.1")

    // ── Adaptive Layout (Tablet/VR support) ──────────────────
    implementation("androidx.window:window:1.2.0")
    implementation("androidx.compose.material3:material3-window-size-class:1.2.0")

    // ── Coroutines ───────────────────────────────────────────
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // ── Permissions ──────────────────────────────────────────
    implementation("com.google.accompanist:accompanist-permissions:0.33.2-alpha")
}
```

---

## Screen-by-Screen Breakdown

---

### Screen 1 — Splash Screen

**File:** `SplashScreen.kt` + `AndroidManifest.xml`

**What it shows:**
- App logo (centered)
- App name: **"Facial Emotion Recognition"**
- Bottom credit: **"Developed by SAKHI DEV"**
- Auto-transitions to Landing Screen after 2.5 seconds

**Implementation approach:**
Use Android 12+ native Splash Screen API for the logo animation, then a custom Compose screen for the "Developed by SAKHI DEV" text at the bottom.

```kotlin
// In themes.xml
<style name="Theme.FER.SplashScreen" parent="Theme.SplashScreen">
    <item name="windowSplashScreenBackground">#0A0A0A</item>
    <item name="windowSplashScreenAnimatedIcon">@drawable/ic_logo</item>
    <item name="windowSplashScreenAnimationDuration">1000</item>
    <item name="postSplashScreenTheme">@style/Theme.FER</item>
</style>

// In MainActivity.kt
val splashScreen = installSplashScreen()
splashScreen.setKeepOnScreenCondition { /* wait for data */ }
```

**UI Design:**
- Background: Deep dark (`#0A0A0A`)
- Logo: Animated face/brain icon, glowing purple/blue gradient
- App name: White, elegant sans-serif font, centered
- Developer credit: Small, muted gray text, bottom of screen

---

### Screen 2 — Landing Screen

**File:** `LandingScreen.kt`

**What it shows:**
- Minimalistic dark background
- Animated logo or icon at the top
- App name and short tagline ("Detect emotions in real-time")
- One prominent CTA button: **"Start Detection"**
- Button navigates to Camera Screen

**UI Design:**
- Background: Dark gradient (`#0A0A0A` → `#1A1A2E`)
- Accent color: Electric purple / cyan glow
- Button: Rounded pill shape, glowing border animation
- Clean, spacious layout — no clutter

```kotlin
@Composable
fun LandingScreen(onStartClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0A0A), Color(0xFF1A1A2E))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Animated logo
            LogoAnimation()
            Spacer(modifier = Modifier.height(32.dp))
            Text("Facial Emotion Recognition", style = MaterialTheme.typography.headlineMedium)
            Text("Detect emotions in real-time", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(48.dp))
            GlowingButton(text = "Start Detection", onClick = onStartClick)
        }
    }
}
```

---

### Screen 3 — Camera + Detection Screen

**File:** `CameraScreen.kt`

**What it shows:**
- Full-screen camera preview (front camera)
- Face bounding box overlay (drawn on Canvas over preview)
- Live emotion result card at the bottom:
  - Emotion icon (😊 😢 😰 😐)
  - Emotion label: HAPPY / SAD / NERVOUS / NEUTRAL
  - Confidence percentage bar
  - Eye state label: 👁 EYES OPEN / 😑 EYES CLOSED
- Back button (top-left)

**UI Layout:**

```
┌─────────────────────────────────┐
│  ←  Back                        │
│                                 │
│     [CAMERA PREVIEW]            │
│         ┌───────┐               │
│         │ FACE  │  ← overlay   │
│         └───────┘               │
│                                 │
│  ┌──────────────────────────┐   │
│  │  😊  HAPPY    89%  ████░ │   │
│  │  👁  Eyes: OPEN          │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
```

---

## ML Model Integration

### Model 1 — TensorFlow Lite (Emotion Classification)

**Model:** FER+ (Facial Expression Recognition Plus)
**Download:** Available from ONNX Model Zoo → convert to TFLite

**Emotions detected:**
| Label | Description |
|---|---|
| HAPPY | Smiling, positive expression |
| SAD | Downturned mouth, drooping features |
| NERVOUS | Tensed brow, worried expression |
| NEUTRAL | Blank expression |
| ANGRY | (available, can be mapped to NERVOUS) |
| SURPRISED | (available, optional to show) |

**Input:** 64×64 grayscale face crop
**Output:** Array of 8 emotion probabilities

```kotlin
class TFLiteEmotionClassifier(context: Context) {

    private val interpreter: Interpreter

    init {
        val model = loadModelFile(context, "fer_model.tflite")
        interpreter = Interpreter(model)
    }

    fun classify(bitmap: Bitmap): EmotionResult {
        val input = preprocessBitmap(bitmap)        // resize to 64x64, normalize
        val output = Array(1) { FloatArray(8) }
        interpreter.run(input, output)
        return mapToEmotion(output[0])
    }

    private fun preprocessBitmap(bitmap: Bitmap): Array<Array<Array<FloatArray>>> {
        val resized = Bitmap.createScaledBitmap(bitmap, 64, 64, true)
        // Convert to grayscale float array, normalize 0-1
        // Shape: [1, 64, 64, 1]
    }

    private fun mapToEmotion(scores: FloatArray): EmotionResult {
        val labels = listOf("ANGRY","DISGUST","FEAR","HAPPY","NEUTRAL","SAD","SURPRISED","NERVOUS")
        val maxIndex = scores.indices.maxByOrNull { scores[it] } ?: 0
        return EmotionResult(
            emotion = labels[maxIndex],
            confidence = scores[maxIndex]
        )
    }
}
```

---

### Model 2 — MediaPipe Face Landmarker (Eye Detection)

**Purpose:** Detect 468 facial landmarks → extract eye landmarks → calculate Eye Aspect Ratio (EAR) to determine if eyes are open or closed.

**Eye Aspect Ratio (EAR) Formula:**
```
EAR = (|p2 - p6| + |p3 - p5|) / (2 * |p1 - p4|)

If EAR < 0.2  →  EYES CLOSED
If EAR ≥ 0.2  →  EYES OPEN
```

```kotlin
class EyeStateDetector {

    fun detectEyeState(faceLandmarks: List<NormalizedLandmark>): EyeState {
        // Left eye landmark indices in MediaPipe: 362, 385, 387, 263, 373, 380
        val leftEAR = calculateEAR(faceLandmarks, LEFT_EYE_INDICES)
        val rightEAR = calculateEAR(faceLandmarks, RIGHT_EYE_INDICES)
        val avgEAR = (leftEAR + rightEAR) / 2f

        return if (avgEAR < EAR_THRESHOLD) EyeState.CLOSED else EyeState.OPEN
    }

    private fun calculateEAR(landmarks: List<NormalizedLandmark>, indices: IntArray): Float {
        // Calculate distances between vertical and horizontal eye points
        val vertical1 = distance(landmarks[indices[1]], landmarks[indices[5]])
        val vertical2 = distance(landmarks[indices[2]], landmarks[indices[4]])
        val horizontal = distance(landmarks[indices[0]], landmarks[indices[3]])
        return (vertical1 + vertical2) / (2f * horizontal)
    }

    companion object {
        const val EAR_THRESHOLD = 0.2f
        val LEFT_EYE_INDICES = intArrayOf(362, 385, 387, 263, 373, 380)
        val RIGHT_EYE_INDICES = intArrayOf(33, 160, 158, 133, 153, 144)
    }
}
```

---

## Camera Integration (CameraX)

```kotlin
class FrameAnalyzer(
    private val emotionClassifier: TFLiteEmotionClassifier,
    private val faceDetector: MediaPipeFaceDetector,
    private val eyeDetector: EyeStateDetector,
    private val onResult: (EmotionResult, EyeState, RectF?) -> Unit
) : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
        val bitmap = imageProxy.toBitmap()

        // Step 1: Detect face with MediaPipe
        val faceResult = faceDetector.detect(bitmap)

        if (faceResult.isNotEmpty()) {
            val face = faceResult.first()

            // Step 2: Crop face region
            val faceBitmap = cropFace(bitmap, face.boundingBox())

            // Step 3: Classify emotion
            val emotion = emotionClassifier.classify(faceBitmap)

            // Step 4: Detect eye state
            val eyeState = eyeDetector.detectEyeState(face.faceLandmarks().first())

            // Step 5: Emit result
            onResult(emotion, eyeState, face.boundingBox())
        }

        imageProxy.close()
    }
}
```

---

## Data Models

```kotlin
// EmotionResult.kt
data class EmotionResult(
    val emotion: String,        // "HAPPY", "SAD", "NERVOUS", "NEUTRAL"
    val confidence: Float,      // 0.0 to 1.0
    val timestamp: Long = System.currentTimeMillis()
)

// EyeState.kt
enum class EyeState {
    OPEN, CLOSED, UNKNOWN
}

// FaceData.kt
data class FaceData(
    val boundingBox: RectF,
    val landmarks: List<PointF>,
    val emotion: EmotionResult,
    val eyeState: EyeState
)

// UiState.kt
sealed class CameraUiState {
    object Idle : CameraUiState()
    object NoFaceDetected : CameraUiState()
    data class FaceDetected(val faceData: FaceData) : CameraUiState()
    data class Error(val message: String) : CameraUiState()
}
```

---

## Device Support (Phone, Tablet, VR)

### Adaptive Layout with WindowSizeClass

```kotlin
@Composable
fun CameraScreen() {
    val windowSizeClass = calculateWindowSizeClass(activity)

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact  -> PhoneLayout()    // Phones
        WindowWidthSizeClass.Medium   -> TabletLayout()   // Tablets (portrait)
        WindowWidthSizeClass.Expanded -> TabletLayout()   // Tablets (landscape) / VR
    }
}
```

### Phone Layout
- Camera takes 70% of screen height
- Emotion card at bottom 30%
- Portrait optimized

### Tablet Layout
- Camera on left 60%
- Emotion details panel on right 40%
- Side-by-side landscape layout

### VR Glasses (Android-based, e.g., Meta Quest, Pico)
- Sideload the APK directly
- App renders as a flat 2D panel in VR space
- Use expanded layout (same as landscape tablet)
- Large text sizes for readability at a distance
- High contrast dark theme for VR displays
- Note: For full stereoscopic VR, a Unity build would be needed separately

---

## AndroidManifest.xml Permissions

```xml
<uses-permission android:name="android.permission.CAMERA" />

<uses-feature
    android:name="android.hardware.camera"
    android:required="true" />
<uses-feature
    android:name="android.hardware.camera.front"
    android:required="false" />

<application
    android:theme="@style/Theme.FER.SplashScreen"
    ... >
    <activity
        android:name=".MainActivity"
        android:windowSoftInputMode="adjustResize"
        android:screenOrientation="fullUser" />
</application>
```

---

## Development Phases

### Phase 1 — Project Setup (Week 1)
- [ ] Create Android project in Android Studio
- [ ] Configure Gradle with all dependencies
- [ ] Set up MVVM architecture structure
- [ ] Configure Hilt dependency injection
- [ ] Set up Jetpack Navigation
- [ ] Define Material3 theme, colors, typography

### Phase 2 — Splash & Landing UI (Week 1-2)
- [ ] Implement Splash Screen with Android 12+ API
- [ ] Design and build app logo (vector drawable)
- [ ] Build Landing Screen with animated UI
- [ ] Implement navigation from Landing → Camera

### Phase 3 — Camera Integration (Week 2)
- [ ] Set up CameraX with front camera
- [ ] Build Compose camera preview component
- [ ] Implement runtime camera permission handling
- [ ] Set up ImageAnalysis use case for frame processing

### Phase 4 — ML Integration (Week 3)
- [ ] Download and convert FER+ model to TFLite format
- [ ] Download MediaPipe Face Landmarker model (.task file)
- [ ] Implement `TFLiteEmotionClassifier`
- [ ] Implement `MediaPipeFaceDetector`
- [ ] Implement `EyeStateDetector` with EAR algorithm
- [ ] Wire ML pipeline into FrameAnalyzer

### Phase 5 — Results UI (Week 3-4)
- [ ] Build face bounding box overlay (Canvas drawing)
- [ ] Build emotion result card component
- [ ] Build emotion icon + confidence bar
- [ ] Build eye state indicator
- [ ] Wire ViewModel to UI with StateFlow

### Phase 6 — Adaptive Layouts (Week 4)
- [ ] Implement WindowSizeClass detection
- [ ] Build Phone layout
- [ ] Build Tablet/VR layout
- [ ] Test on Android emulator (phone & tablet)

### Phase 7 — Polish & Testing (Week 5)
- [ ] UI animations and transitions
- [ ] Performance optimization (GPU delegate for TFLite)
- [ ] Edge case handling (no face, multiple faces, low light)
- [ ] Unit tests for ML classifiers
- [ ] UI tests with Espresso/Compose testing

### Phase 8 — Build & Deploy (Week 5-6)
- [ ] Generate signed APK / AAB
- [ ] Test on real Android devices
- [ ] Test sideload on Android VR headset (if available)
- [ ] Optional: Publish to Google Play Store

---

## Testing Strategy

| Type | Tool | What to Test |
|---|---|---|
| Unit Tests | JUnit 5 + Mockk | EAR calculation, emotion mapping, ViewModel state |
| Integration Tests | Hilt Testing | Repository + ML pipeline integration |
| UI Tests | Compose Testing | Screen rendering, button clicks, navigation |
| Camera Tests | Manual | Real device, different lighting conditions |
| Performance Tests | Android Profiler | FPS, memory usage, CPU during ML inference |

---

## Build & Deployment

### Debug APK
```bash
./gradlew assembleDebug
# Output: app/build/outputs/apk/debug/app-debug.apk
```

### Release APK (Signed)
```bash
./gradlew assembleRelease
# Output: app/build/outputs/apk/release/app-release.apk
```

### Release Bundle (Google Play)
```bash
./gradlew bundleRelease
# Output: app/build/outputs/bundle/release/app-release.aab
```

### Sideload to VR Device
```bash
adb install app-debug.apk
# Or use SideQuest for Meta Quest devices
```

---

## Performance Optimization Tips

- Use **TFLite GPU Delegate** for faster inference on mobile GPUs
- Run ML inference on a **background thread** (not main thread)
- Process every **3rd frame** instead of every frame to reduce CPU load
- Use **ImageProxy** YUV format (faster than converting to Bitmap every frame)
- Cache the TFLite **Interpreter** — don't recreate it per frame
- Use **MediaPipe's built-in GPU acceleration** when available

---

## Key Android Studio Settings

- **JDK Version:** 17
- **Kotlin Version:** 1.9.x
- **Android Gradle Plugin:** 8.2.x
- **Emulator:** Pixel 7 (API 34) + Pixel Tablet (API 34)
- **Enable:** Hardware acceleration in AVD for camera emulation

---

## Resources & References

| Resource | Link |
|---|---|
| MediaPipe Android Docs | developers.google.com/mediapipe |
| TensorFlow Lite Android | tensorflow.org/lite/android |
| CameraX Documentation | developer.android.com/training/camerax |
| Jetpack Compose | developer.android.com/jetpack/compose |
| FER+ Model (ONNX) | github.com/microsoft/FERPlus |
| Material Design 3 | m3.material.io |
| WindowSizeClass | developer.android.com/guide/topics/large-screens |

---

*Document Version: 1.0 | Prepared for SAKHI DEV | Facial Emotion Recognition Android App*
