package com.sakhidev.fer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakhidev.fer.model.EyeState
import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.ui.theme.*

/**
 * Compact badge showing the emotion label and eye state.
 */
@Composable
fun EmotionBadge(
    emotion: EmotionResult,
    eyeState: EyeState,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = emotionColor(emotion.emotion).copy(alpha = 0.18f),
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Emotion badge
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = emotionColor(emotion.emotion).copy(alpha = 0.25f)
        ) {
            Text(
                text = "${emotion.emoji} ${emotion.emotion}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = emotionColor(emotion.emotion),
                    fontSize = 12.sp
                )
            )
        }

        // Eye state badge
        Surface(
            shape = RoundedCornerShape(50.dp),
            color = eyeStateColor(eyeState).copy(alpha = 0.25f)
        ) {
            Text(
                text = "${eyeState.emoji} ${eyeState.label}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelLarge.copy(
                    color = eyeStateColor(eyeState),
                    fontSize = 12.sp
                )
            )
        }
    }
}

fun emotionColor(emotion: String): Color = when (emotion) {
    "HAPPY"   -> EmotionHappy
    "SAD"     -> EmotionSad
    "NERVOUS" -> EmotionNervous
    "ANGRY"   -> EmotionAngry
    else      -> EmotionNeutral
}

fun eyeStateColor(eyeState: EyeState): Color = when (eyeState) {
    EyeState.OPEN    -> EyeOpen
    EyeState.CLOSED  -> EyeClosed
    EyeState.UNKNOWN -> EmotionNeutral
}
