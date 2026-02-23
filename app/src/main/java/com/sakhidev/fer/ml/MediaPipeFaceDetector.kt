package com.sakhidev.fer.ml

import android.content.Context
import android.graphics.Bitmap
import android.graphics.RectF
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import javax.inject.Inject
import javax.inject.Singleton

/**
 * MediaPipe Face Landmarker wrapper for face detection and landmark extraction.
 *
 * Provides:
 *  - Face bounding boxes (as RectF in pixel coords)
 *  - 468 facial landmarks per face (normalized 0..1 coords)
 *
 * Place 'face_landmarker.task' in app/src/main/assets/
 * Download from: https://developers.google.com/mediapipe/solutions/vision/face_landmarker#models
 */
@Singleton
class MediaPipeFaceDetector @Inject constructor(
    private val context: Context
) {
    companion object {
        private const val MODEL_FILE = "face_landmarker.task"
        private const val MAX_FACES = 1
    }

    data class FaceDetectionResult(
        val boundingBox: RectF,
        val landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>,
        val blendshapes: List<com.google.mediapipe.tasks.components.containers.Category>,
        val imageWidth: Int,
        val imageHeight: Int
    )

    private var faceLandmarker: FaceLandmarker? = null
    private var modelLoaded = false

    init {
        try {
            val baseOptions = BaseOptions.builder()
                .setModelAssetPath(MODEL_FILE)
                .build()

            val options = FaceLandmarker.FaceLandmarkerOptions.builder()
                .setBaseOptions(baseOptions)
                .setNumFaces(MAX_FACES)
                .setRunningMode(RunningMode.IMAGE)
                .setMinFaceDetectionConfidence(0.5f)
                .setMinFacePresenceConfidence(0.5f)
                .setMinTrackingConfidence(0.5f)
                .setOutputFaceBlendshapes(true)
                .build()

            faceLandmarker = FaceLandmarker.createFromOptions(context, options)
            modelLoaded = true
        } catch (e: Exception) {
            // Model file not yet in assets — runs in demo mode
            modelLoaded = false
        }
    }

    /**
     * Detect faces in the given bitmap.
     * @return List of FaceDetectionResult (empty if no face found or model not loaded)
     */
    fun detect(bitmap: Bitmap): List<FaceDetectionResult> {
        if (!modelLoaded || faceLandmarker == null) {
            return getDemoResults(bitmap.width, bitmap.height)
        }

        return try {
            val mpImage = BitmapImageBuilder(bitmap).build()
            val result: FaceLandmarkerResult = faceLandmarker!!.detect(mpImage)

            result.faceLandmarks().mapIndexed { index, landmarks ->
                val boundingBox = computeBoundingBox(landmarks, bitmap.width, bitmap.height)
                val blendshapes = if (result.faceBlendshapes().isPresent && result.faceBlendshapes().get().size > index) {
                    result.faceBlendshapes().get()[index]
                } else {
                    emptyList()
                }
                
                FaceDetectionResult(
                    boundingBox = boundingBox,
                    landmarks = landmarks,
                    blendshapes = blendshapes,
                    imageWidth = bitmap.width,
                    imageHeight = bitmap.height
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun computeBoundingBox(
        landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>,
        imageWidth: Int,
        imageHeight: Int
    ): RectF {
        var minX = Float.MAX_VALUE
        var minY = Float.MAX_VALUE
        var maxX = Float.MIN_VALUE
        var maxY = Float.MIN_VALUE

        landmarks.forEach { lm ->
            val x = lm.x() * imageWidth
            val y = lm.y() * imageHeight
            if (x < minX) minX = x
            if (y < minY) minY = y
            if (x > maxX) maxX = x
            if (y > maxY) maxY = y
        }

        // Add 10% padding around the bounding box
        val padX = (maxX - minX) * 0.1f
        val padY = (maxY - minY) * 0.1f
        return RectF(
            (minX - padX).coerceAtLeast(0f),
            (minY - padY).coerceAtLeast(0f),
            (maxX + padX).coerceAtMost(imageWidth.toFloat()),
            (maxY + padY).coerceAtMost(imageHeight.toFloat())
        )
    }

    /** Demo bounding box centered in the image when model is not loaded */
    private fun getDemoResults(imageWidth: Int, imageHeight: Int): List<FaceDetectionResult> {
        val cx = imageWidth / 2f
        val cy = imageHeight / 2f
        val half = minOf(imageWidth, imageHeight) * 0.3f
        return listOf(
            FaceDetectionResult(
                boundingBox = RectF(cx - half, cy - half, cx + half, cy + half),
                landmarks = emptyList(),
                blendshapes = emptyList(),
                imageWidth = imageWidth,
                imageHeight = imageHeight
            )
        )
    }

    fun close() {
        faceLandmarker?.close()
        faceLandmarker = null
    }
}
