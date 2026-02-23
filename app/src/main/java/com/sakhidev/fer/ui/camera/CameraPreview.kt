package com.sakhidev.fer.ui.camera

import android.view.ViewGroup
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.sakhidev.fer.camera.CameraManager

/**
 * Compose wrapper for CameraX PreviewView.
 * Uses AndroidView to integrate the SurfaceView-based camera preview.
 */
@Composable
fun CameraPreview(
    cameraManager: CameraManager,
    modifier: Modifier = Modifier,
    analyzer: androidx.camera.core.ImageAnalysis.Analyzer
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                scaleType = PreviewView.ScaleType.FILL_CENTER
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }.also { previewView ->
                cameraManager.startCamera(lifecycleOwner, previewView, analyzer)
            }
        },
        modifier = modifier
    )
}
