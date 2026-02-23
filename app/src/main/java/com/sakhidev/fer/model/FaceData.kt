package com.sakhidev.fer.model

import android.graphics.PointF
import android.graphics.RectF
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark

data class FaceData(
    val boundingBox: RectF,
    val landmarks: List<NormalizedLandmark> = emptyList(),
    val emotion: EmotionResult,
    val eyeState: EyeState,
    val imageWidth: Int,
    val imageHeight: Int
)
