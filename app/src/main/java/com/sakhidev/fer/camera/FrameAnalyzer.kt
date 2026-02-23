package com.sakhidev.fer.camera

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mediapipe.tasks.components.containers.Category
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.sakhidev.fer.ml.MediaPipeFaceDetector
import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.model.EyeState
import javax.inject.Inject

/**
 * Per-frame ML pipeline.
 * Runs: Face detection → face crop → emotion classification → eye state detection
 * Reports results via [onResult] callback.
 *
 * Processes every 3rd frame to reduce CPU load.
 */
class FrameAnalyzer(
    private val faceDetector: MediaPipeFaceDetector,
    private val onResult: (EmotionResult, EyeState, List<NormalizedLandmark>, RectF?, Int, Int) -> Unit
) : ImageAnalysis.Analyzer {

    private var frameCount = 0

    override fun analyze(imageProxy: ImageProxy) {
        frameCount++

        // Process every 3rd frame
        if (frameCount % 3 != 0) {
            imageProxy.close()
            return
        }

        try {
            val bitmap = imageProxy.toBitmap().rotateBitmap(imageProxy.imageInfo.rotationDegrees.toFloat())

            // Step 1: Detect face
            val detectionResults = faceDetector.detect(bitmap)

            if (detectionResults.isNotEmpty()) {
                val faceResult = detectionResults.first()
                val boundingBox = faceResult.boundingBox

                // Step 2: Use Blendshapes for analytics
                val emotion = detectEmotion(faceResult.blendshapes)
                val eyeState = detectEyeState(faceResult.blendshapes)
                
                // Step 3: Scale bounding box to preview coords
                onResult(
                    emotion, 
                    eyeState, 
                    faceResult.landmarks, 
                    boundingBox, 
                    bitmap.width, 
                    bitmap.height
                )
            } else {
                onResult(EmotionResult("NEUTRAL", 1.0f), EyeState.OPEN, emptyList(), null, 0, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            imageProxy.close()
        }
    }

    private fun detectEyeState(blendshapes: List<Category>): EyeState {
        val blinkLeft  = blendshapes.find { it.categoryName() == "eyeBlinkLeft"  }?.score() ?: 0f
        val blinkRight = blendshapes.find { it.categoryName() == "eyeBlinkRight" }?.score() ?: 0f
        val avgBlink = (blinkLeft + blinkRight) / 2f
        return if (avgBlink > 0.4f) EyeState.CLOSED else EyeState.OPEN
    }

    private fun detectEmotion(blendshapes: List<Category>): EmotionResult {
        fun score(name: String) = blendshapes.find { it.categoryName() == name }?.score() ?: 0f

        val happy = (score("mouthSmileLeft") + score("mouthSmileRight") + score("cheekSquintLeft") + score("cheekSquintRight")) / 4f
        val sad = (score("mouthFrownLeft") + score("mouthFrownRight") + score("browDownLeft") + score("browDownRight")) / 4f
        val angry = (score("browDownLeft") + score("browDownRight") + score("noseSneerLeft") + score("noseSneerRight") + score("mouthPressLeft") + score("mouthPressRight")) / 6f
        val nervous = (score("browInnerUp") + score("eyeWideLeft") + score("eyeWideRight") + score("jawOpen")) / 4f

        val scores = mapOf(
            "HAPPY" to happy,
            "SAD" to sad,
            "ANGRY" to angry,
            "NERVOUS" to nervous
        )

        val dominant = scores.maxByOrNull { it.value }

        return if (dominant != null && dominant.value > 0.25f) {
            EmotionResult(dominant.key, dominant.value)
        } else {
            EmotionResult("NEUTRAL", 1f - (scores.values.maxOrNull() ?: 0f))
        }
    }

    private fun Bitmap.rotateBitmap(degrees: Float): Bitmap {
        if (degrees == 0f) return this
        val matrix = Matrix().apply { postRotate(degrees) }
        return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
    }
}
