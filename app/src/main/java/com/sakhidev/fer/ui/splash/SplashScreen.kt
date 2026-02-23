package com.sakhidev.fer.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.sakhidev.fer.R
import com.sakhidev.fer.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Splash Screen — shown briefly on app launch.
 * Shows animated logo, app name, and developer credit.
 * Auto-navigates to Landing after 2.5 seconds.
 */
@Composable
fun SplashScreen(onNavigateToLanding: () -> Unit) {

    // ── Animations ─────────────────────────────────────────────
    val logoScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    var textAlpha by remember { mutableStateOf(0f) }
    val animatedTextAlpha by animateFloatAsState(
        targetValue = textAlpha,
        animationSpec = tween(800),
        label = "text_alpha"
    )

    var showButton by remember { mutableStateOf(false) }
    val buttonAlpha by animateFloatAsState(
        targetValue = if (showButton) 1f else 0f,
        animationSpec = tween(1000),
        label = "btn_alpha"
    )
    val buttonScale by animateFloatAsState(
        targetValue = if (showButton) 1f else 0.8f,
        animationSpec = spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow),
        label = "btn_scale"
    )

    LaunchedEffect(Unit) {
        delay(500)
        textAlpha = 1f
        delay(1000)
        showButton = true
    }

    // ── UI ─────────────────────────────────────────────────────
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        BackgroundDeep,
                        BackgroundDark
                    ),
                    radius = 1200f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Logo ──────────────────────────────────────────────
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(140.dp)
                    .scale(logoScale)
            ) {
                // Pulse glow ring
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ElectricPurple.copy(alpha = glowAlpha * 0.3f),
                                    ElectricPurple.copy(alpha = 0f)
                                )
                            )
                        )
                )
                // Image replacing the video player
                Image(
                    painter = painterResource(id = R.drawable.face),
                    contentDescription = "Face Logo",
                    modifier = Modifier.size(90.dp)
                )
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ── App Name ─────────────────────────────────────────
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    letterSpacing = 1.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(animatedTextAlpha)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.tagline),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = CyanAccent.copy(alpha = 0.8f)
                ),
                modifier = Modifier.alpha(animatedTextAlpha)
            )

            Spacer(modifier = Modifier.height(64.dp))

            // ── Continue Button ──────────────────────────────────
            Button(
                onClick = onNavigateToLanding,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(56.dp)
                    .alpha(buttonAlpha)
                    .scale(buttonScale),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricPurple,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "CONTINUE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Demo Version\nDeveloped for\nMohamad ElJachi",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextMuted.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .alpha(buttonAlpha)
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── Developer Credit ──────────────────────────────────
            Text(
                text = stringResource(R.string.developer_credit),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    letterSpacing = 1.5.sp
                ),
                modifier = Modifier
                    .padding(bottom = 80.dp) // Moved up significantly
                    .alpha(animatedTextAlpha)
            )
        }
    }
}
