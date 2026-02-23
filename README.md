# Facial Emotion Recognition (FER) - Android App

A professional, high-precision real-time facial analytics application built with **Jetpack Compose** and **MediaPipe**. This app utilizes advanced AI to detect emotions and track eye states with superior accuracy by leveraging facial blendshapes rather than traditional static classification.

## 🚀 Features

- **High-Precision Emotion Detection**: Real-time classification of **Happy, Sad, Angry, Nervous, and Neutral** states.
- **Advanced Eye Tracking**: Precise detection of Eye Blink and Eye State (Open/Closed) using localized facial muscle triggers.
- **468-Point Landmark Mesh**: A professional "Tech Mesh" overlay that visualizes the face's topological structure in real-time.
- **Modern UI/UX**: Premium dark-mode aesthetic with glassmorphism effects, smooth animations, and responsive layouts for mobile and tablet.
- **Performance Optimized**: Built with a single-pass ML pipeline using MediaPipe, ensuring low CPU usage and high frame rates.

## 🛠 Tech Stack

- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (100% Kotlin-based modern UI)
- **Programming Language**: [Kotlin](https://kotlinlang.org/) (Coroutines, Flow, Hilt)
- **ML Engine**: [MediaPipe Face Landmarker](https://developers.google.com/mediapipe/solutions/vision/face_landmarker)
- **Detection Method**: **Weighted Facial Blendshapes** (Measuring 52 specialized facial muscle movements for clinical-grade analytics)
- **Camera**: [CameraX](https://developer.android.com/training/camerax) (Efficient frame processing and hardware abstraction)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

## 🧠 AI/ML Methodology

Unlike traditional FER apps that use a single classifier model, this app uses the **MediaPipe V2** pipeline to extract 52 facial blendshapes. 

- **Weighted Emotion Logic**: We map multiple blendshape scores (like `mouthSmileLeft`, `cheekSquintRight`, `browDownLeft`) using weighted heuristics to determine the dominant emotional state. This approach is significantly more robust against variations in lighting and head orientation.
- **Eye State Precision**: Detection follows the `eyeBlink` blendshape triggers, providing a much higher reliability for blink detection compared to standard EAR (Eye Aspect Ratio) methods.

## 📂 Project Structure

- `/ml`: Core MediaPipe wrapper and blendshape-to-emotion mapping logic.
- `/camera`: CameraX implementation and high-speed FrameAnalyzer.
- `/ui/theme`: Custom design system (Electric Purple & Cyan Accent palette).
- `/ui/components`: Reusable Compose components (EmotionCard, FaceOverlay, etc.).
- `/viewmodel`: State management for seamless UI updates.

## 👨‍💻 Developer

Developed with ❤️ by **Muhammad Anas**.

- **GitHub**: [@anasibnyounis](https://github.com/anasibnyounis)
- **LinkedIn**: [muhammad-anas](https://www.linkedin.com/in/anasibnyounis/)

---

### Getting Started

1. Clone the repository:
   ```bash
   git clone https://github.com/Anasibnyounis/facial-emotions-recognition-android-app.git
   ```
2. Open in Android Studio (Ladybug or newer).
3. Ensure the `face_landmarker.task` is in the `app/src/main/assets` directory.
4. Build and run on a physical Android device or emulator with camera support.

---
© 2026 Muhammad Anas | [sakhidev](https://github.com/anasibnyounis)
