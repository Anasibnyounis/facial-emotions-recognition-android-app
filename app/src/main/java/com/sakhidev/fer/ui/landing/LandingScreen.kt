package com.sakhidev.fer.ui.landing

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.sakhidev.fer.R
import com.sakhidev.fer.ui.theme.*

/**
 * Landing Screen — entry point after splash.
 * Features:
 * - Dark gradient background
 * - Animated pulsing logo
 * - App name + tagline
 * - Glowing "Start Detection" button
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LandingScreen(onStartDetection: () -> Unit) {

    // ── Animations ─────────────────────────────────────────────
    val infiniteTransition = rememberInfiniteTransition(label = "landing")

    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1600, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_pulse"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val buttonBorderAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "btn_border"
    )

    val floatOffset by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_float"
    )

    // ── UI ─────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BackgroundDark, BackgroundDeep, BackgroundDark)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 40.dp)
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            // ── Logo area ─────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(200.dp)
                    .offset(y = floatOffset.dp)
                    .scale(logoScale)
            ) {
                // Outer glow
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    ElectricPurple.copy(alpha = glowAlpha * 0.4f),
                                    CyanAccent.copy(alpha = glowAlpha * 0.1f),
                                    ElectricPurple.copy(alpha = 0f)
                                )
                            )
                        )
                )
                // Image replacing the video player
                Image(
                    painter = painterResource(id = R.drawable.face),
                    contentDescription = "Face Logo",
                    modifier = Modifier.size(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ── App Name ──────────────────────────────────────────
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                    color = TextPrimary
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ── Tagline ───────────────────────────────────────────
            Text(
                text = stringResource(R.string.tagline),
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextSecondary,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Feature chips ──────────────────────────────────────
            FlowRow(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxItemsInEachRow = 3
            ) {
                FeatureChip("😊 Emotion AI")
                FeatureChip("👁 Eye Tracking")
                FeatureChip("⚡ Real-time")
            }

            Spacer(modifier = Modifier.height(48.dp))

            // ── CTA Button ────────────────────────────────────────
            Button(
                onClick = onStartDetection,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .border(
                        width = 2.dp,
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                ElectricPurple.copy(alpha = buttonBorderAlpha),
                                CyanAccent.copy(alpha = buttonBorderAlpha)
                            )
                        ),
                        shape = RoundedCornerShape(100.dp)
                    ),
                shape = RoundedCornerShape(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SurfaceDark.copy(alpha = 0.6f),
                    contentColor = TextPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 2.dp
                )
            ) {
                Text(
                    text = stringResource(R.string.start_detection),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.5.sp,
                        color = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Developer credit ──────────────────────────────────
            Text(
                text = stringResource(R.string.developer_credit),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(12.dp))
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun FeatureChip(label: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = SurfaceElevated.copy(alpha = 0.3f), // Glassy effect
        border = BorderStroke(
            1.dp, 
            Brush.linearGradient(
                listOf(ElectricPurple.copy(alpha = 0.5f), CyanAccent.copy(alpha = 0.5f))
            )
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
