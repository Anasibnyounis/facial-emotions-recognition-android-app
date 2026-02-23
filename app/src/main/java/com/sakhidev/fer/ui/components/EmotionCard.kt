package com.sakhidev.fer.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.border
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.model.EyeState
import com.sakhidev.fer.ui.theme.*

/**
 * Bottom result card showing:
 * - Emotion emoji + label
 * - Animated confidence progress bar
 * - Eye state indicator
 */
@Composable
fun EmotionCard(
    emotion: EmotionResult,
    eyeState: EyeState,
    modifier: Modifier = Modifier
) {
    val animatedConfidence by animateFloatAsState(
        targetValue = emotion.confidence,
        animationSpec = tween(durationMillis = 400, easing = EaseOutCubic),
        label = "confidence_anim"
    )

    val cardColor = emotionColor(emotion.emotion)

    val infiniteTransition = rememberInfiniteTransition(label = "eye_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(32.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SurfaceDark.copy(alpha = 0.90f), BackgroundDeep.copy(alpha = 0.95f))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(cardColor.copy(alpha = 0.4f), Color.Transparent)
                ),
                shape = RoundedCornerShape(32.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // ── Emotion Row ─────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Emoji
                    Text(
                        text = emotion.emoji,
                        fontSize = 40.sp
                    )
                    Column {
                        Text(
                            text = emotion.emotion,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = cardColor,
                                letterSpacing = 2.sp
                            )
                        )
                        Text(
                            text = "Confidence",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted
                            )
                        )
                    }
                }

                // Confidence percentage
                Text(
                    text = "${emotion.confidencePercent}%",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = cardColor
                    )
                )
            }

            // ── Confidence Bar ───────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(SurfaceElevated)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedConfidence)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50.dp))
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(cardColor.copy(alpha = 0.6f), cardColor)
                            )
                        )
                )
            }


            // ── Eye State Row ────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = eyeState.emoji, fontSize = 20.sp)
                Text(
                    text = eyeState.label,
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = eyeStateColor(eyeState),
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                // Dot indicator
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(eyeStateColor(eyeState).copy(alpha = pulseAlpha))
                        .border(
                            width = 1.dp,
                            color = eyeStateColor(eyeState),
                            shape = RoundedCornerShape(50.dp)
                        )
                )
            }
        }
    }
}

