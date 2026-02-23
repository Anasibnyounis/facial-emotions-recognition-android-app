package com.sakhidev.fer.model

data class EmotionResult(
    val emotion: String = "NEUTRAL",    // "HAPPY", "SAD", "NERVOUS", "NEUTRAL"
    val confidence: Float = 0f,          // 0.0 to 1.0
    val timestamp: Long = System.currentTimeMillis()
) {
    val emoji: String
        get() = when (emotion) {
            "HAPPY" -> "😊"
            "SAD" -> "😢"
            "NERVOUS" -> "😰"
            "ANGRY" -> "😠"
            "SURPRISED" -> "😮"
            "DISGUST" -> "🤢"
            "FEAR" -> "😱"
            else -> "😐" // NEUTRAL
        }

    val confidencePercent: Int
        get() = (confidence * 100).toInt()
}
