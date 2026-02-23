# ⚠️ MODEL FILES REQUIRED — READ BEFORE RUNNING

This folder should contain two ML model files that are NOT included
in the repository due to their size:

## 1. `fer_model.tflite` — Emotion Classification Model
- Based on FER+ (Facial Expression Recognition Plus)
- **Download source:** https://github.com/microsoft/FERPlus
  - Download the ONNX model: `FER_static_ResNet50_AffectNet.onnx` (or similar)
  - Convert to TFLite using TensorFlow:
    ```python
    import tensorflow as tf
    converter = tf.lite.TFLiteConverter.from_saved_model(saved_model_path)
    converter.optimizations = [tf.lite.Optimize.DEFAULT]
    tflite_model = converter.convert()
    with open('fer_model.tflite', 'wb') as f:
        f.write(tflite_model)
    ```
- Expected input: `[1, 64, 64, 1]` float32 grayscale image (0–1 range)
- Expected output: `[1, 8]` float32 emotion probabilities

## 2. `face_landmarker.task` — MediaPipe Face Landmarker
- **Download directly from Google:**
  https://storage.googleapis.com/mediapipe-models/face_landmarker/face_landmarker/float16/1/face_landmarker.task
- Or from MediaPipe docs:
  https://developers.google.com/mediapipe/solutions/vision/face_landmarker#models

## Without These Files
The app will still compile and run in **demo mode**:
- Emotion classification will cycle through emotions on a timer
- Face detection will show a static centered bounding box
- All UI, camera preview, and animations will work normally

Once you add the model files and sync Gradle, full ML inference will activate automatically.
