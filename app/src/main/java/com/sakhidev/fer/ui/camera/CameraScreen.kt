package com.sakhidev.fer.ui.camera

import android.Manifest
import android.graphics.RectF
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.FaceRetouchingOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.layout.WindowMetricsCalculator
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.sakhidev.fer.camera.CameraManager
import com.sakhidev.fer.model.CameraUiState
import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.model.EyeState
import com.sakhidev.fer.ui.components.EmotionCard
import com.sakhidev.fer.ui.components.FaceOverlay
import com.sakhidev.fer.ui.theme.*
import com.sakhidev.fer.viewmodel.CameraViewModel
import javax.inject.Inject

/**
 * Camera Screen — the main detection screen.
 *
 * Layout:
 *  - Full-screen camera preview (front camera)
 *  - Face bounding box overlay on top
 *  - Bottom card showing emotion + eye state
 *  - Top-left back button
 *  - Permission handling
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(
    onBack: () -> Unit,
    viewModel: CameraViewModel = hiltViewModel(),
    cameraManager: CameraManager = hiltViewModel<CameraViewModel>().let {
        // CameraManager is provided via Hilt activity-level
        // In production this would be injected; here we get from DI graph
        rememberCameraManager()
    }
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        when {
            cameraPermission.status.isGranted -> {
                CameraDetectionContent(
                    uiState = uiState,
                    cameraManager = cameraManager,
                    analyzer = viewModel.frameAnalyzer,
                    onBack = onBack
                )
            }

            cameraPermission.status.shouldShowRationale -> {
                PermissionRationaleScreen(
                    onGrantClick = { cameraPermission.launchPermissionRequest() },
                    onBack = onBack
                )
            }

            else -> {
                LaunchedEffect(Unit) {
                    cameraPermission.launchPermissionRequest()
                }
                PermissionRationaleScreen(
                    onGrantClick = { cameraPermission.launchPermissionRequest() },
                    onBack = onBack
                )
            }
        }
    }
}

@Composable
private fun CameraDetectionContent(
    uiState: CameraUiState,
    cameraManager: CameraManager,
    analyzer: androidx.camera.core.ImageAnalysis.Analyzer,
    onBack: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // ── Camera Preview ────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f) // Bound preview to ~75% of screen
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .align(Alignment.TopCenter)
        ) {
            CameraPreview(
                cameraManager = cameraManager,
                modifier = Modifier.fillMaxSize(),
                analyzer = analyzer
            )

            // ── Main Perspective (Standard) ──────────────────────────
            if (uiState is CameraUiState.FaceDetected) {
                val face = uiState.faceData
                FaceOverlay(
                    boundingBox = face.boundingBox,
                    landmarks = face.landmarks, // Show mesh in main view since PIP is gone
                    imageWidth = face.imageWidth,
                    imageHeight = face.imageHeight,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // ── Top gradient scrim ─────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            BackgroundDark.copy(alpha = 0.7f),
                            Color.Transparent
                        )
                    )
                )
        )

        // ── Back button ────────────────────────────────────────
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Back",
                tint = TextPrimary,
                modifier = Modifier.size(22.dp)
            )
        }

        // ── Status label (when scanning) ───────────────────────
        ScanStatusBadge(
            uiState = uiState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 54.dp)
        )

        AnimatedContent(
            targetState = uiState,
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "card_anim",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(start = 16.dp, end = 16.dp, bottom = 24.dp),
            contentKey = { state -> 
                // Only trigger animation when status changes fundamentally
                when(state) {
                    is CameraUiState.FaceDetected -> "detected"
                    is CameraUiState.NoFaceDetected -> "none"
                    else -> "idle"
                }
            }
        ) { state ->
            when (state) {
                is CameraUiState.FaceDetected -> {
                    EmotionCard(
                        emotion = state.faceData.emotion,
                        eyeState = state.faceData.eyeState
                    )
                }
                is CameraUiState.NoFaceDetected -> {
                    NoFaceCard()
                }
                else -> {
                    EmotionCard(
                        emotion = EmotionResult(emotion = "NEUTRAL", confidence = 0f),
                        eyeState = EyeState.UNKNOWN
                    )
                }
            }
        }
    }
}

@Composable
private fun ScanStatusBadge(
    uiState: CameraUiState,
    modifier: Modifier = Modifier
) {
    val (label, color) = when (uiState) {
        is CameraUiState.FaceDetected  -> "● FACE DETECTED"  to CyanAccent
        is CameraUiState.NoFaceDetected -> "○ SCANNING..."   to TextMuted
        is CameraUiState.Idle          -> "○ INITIALIZING"  to TextMuted
        is CameraUiState.Error         -> "⚠ ERROR"          to EmotionNervous
    }
    val animColor by animateColorAsState(targetValue = color, label = "status_color")

    Surface(
        shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
        color = BackgroundDark.copy(alpha = 0.6f),
        modifier = modifier.border(
            width = 1.dp,
            color = animColor.copy(alpha = 0.4f),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp)
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium.copy(
                color = animColor,
                letterSpacing = 1.5.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
private fun NoFaceCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(androidx.compose.foundation.shape.RoundedCornerShape(32.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(SurfaceDark.copy(alpha = 0.90f), BackgroundDeep.copy(alpha = 0.95f))
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(TextMuted.copy(alpha = 0.3f), Color.Transparent)
                ),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(32.dp)
            )
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.FaceRetouchingOff,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = "No face detected",
                style = MaterialTheme.typography.titleMedium.copy(color = TextMuted)
            )
            Text(
                text = "Position your face within the frame",
                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PermissionRationaleScreen(onGrantClick: () -> Unit, onBack: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(40.dp)
        ) {
            Text("📷", fontSize = 64.sp)
            Text(
                text = "Camera Access Required",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold, color = TextPrimary
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "This app needs camera access to detect emotions and track eye state in real-time.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onGrantClick,
                colors = ButtonDefaults.buttonColors(containerColor = ElectricPurple)
            ) {
                Text("Grant Permission", color = Color.White)
            }
            TextButton(onClick = onBack) {
                Text("Go Back", color = TextMuted)
            }
        }
    }
}

/** Helper to retrieve CameraManager from Hilt's component (used in CameraScreen) */
@Composable
private fun rememberCameraManager(): CameraManager {
    val context = LocalContext.current
    return remember { CameraManager(context) }
}
