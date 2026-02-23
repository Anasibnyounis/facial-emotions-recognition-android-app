package com.sakhidev.fer.model

enum class EyeState {
    OPEN,
    CLOSED,
    UNKNOWN;

    val label: String
        get() = when (this) {
            OPEN -> "EYES OPEN"
            CLOSED -> "EYES CLOSED"
            UNKNOWN -> "DETECTING..."
        }

    val emoji: String
        get() = when (this) {
            OPEN -> "👁"
            CLOSED -> "😑"
            UNKNOWN -> "🔍"
        }
}
