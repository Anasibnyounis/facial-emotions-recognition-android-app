package com.sakhidev.fer.ui.components

import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.sakhidev.fer.ui.theme.FaceBoundingBox
import com.sakhidev.fer.ui.theme.GlowBorder
import com.sakhidev.fer.ui.theme.ElectricPurple
import com.sakhidev.fer.ui.theme.CyanAccent

/**
 * Draws a glowing bounding box overlay over the detected face.
 *
 * The bounding box is in camera image pixel coordinates.
 * We scale it to the canvas (preview) size using the previewWidth/Height
 * and imageWidth/imageHeight passed in.
 *
 * Also draws corner accent marks (L-shaped corners) for a premium look.
 */
@Composable
fun FaceOverlay(
    boundingBox: RectF?,
    landmarks: List<NormalizedLandmark>, // Added
    imageWidth: Int,
    imageHeight: Int,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        // ── Drawing the Mesh ──────────────────────────────────────
        landmarks.forEach { lm ->
            val px = size.width - (lm.x() * size.width) // Mirroring
            val py = lm.y() * size.height
            
            drawCircle(
                color = CyanAccent.copy(alpha = 0.8f),
                radius = 2.dp.toPx(),
                center = Offset(px, py)
            )
        }

        boundingBox ?: return@Canvas

        val scaleX = size.width / imageWidth.toFloat()
        val scaleY = size.height / imageHeight.toFloat()

        // Mirror horizontally for front camera (selfie mode)
        val left   = size.width - (boundingBox.right * scaleX)
        val right  = size.width - (boundingBox.left  * scaleX)
        val top    = boundingBox.top    * scaleY
        val bottom = boundingBox.bottom * scaleY

        val boxWidth  = right - left
        val boxHeight = bottom - top
        val cornerLen = minOf(boxWidth, boxHeight) * 0.15f

        // Glow effect — draw a wider, semi-transparent stroke behind
        drawRoundRect(
            color = GlowBorder.copy(alpha = 0.25f),
            topLeft = Offset(left - 4.dp.toPx(), top - 4.dp.toPx()),
            size = Size(boxWidth + 8.dp.toPx(), boxHeight + 8.dp.toPx()),
            cornerRadius = CornerRadius(16.dp.toPx()),
            style = Stroke(width = 8.dp.toPx())
        )

        // Main bounding box stroke
        drawRoundRect(
            color = FaceBoundingBox,
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(12.dp.toPx()),
            style = Stroke(width = 2.dp.toPx())
        )

        // ── Corner accents (L-shapes) ─────────────────────────────
        val strokeW = 3.dp.toPx()
        val strokeColor = FaceBoundingBox
        val paint = Paint().apply {
            color = strokeColor
            strokeWidth = strokeW
        }

        // Top-left corner
        drawLine(strokeColor, Offset(left, top), Offset(left + cornerLen, top), strokeW)
        drawLine(strokeColor, Offset(left, top), Offset(left, top + cornerLen), strokeW)

        // Top-right corner
        drawLine(strokeColor, Offset(right - cornerLen, top), Offset(right, top), strokeW)
        drawLine(strokeColor, Offset(right, top), Offset(right, top + cornerLen), strokeW)

        // Bottom-left corner
        drawLine(strokeColor, Offset(left, bottom - cornerLen), Offset(left, bottom), strokeW)
        drawLine(strokeColor, Offset(left, bottom), Offset(left + cornerLen, bottom), strokeW)

        // Bottom-right corner
        drawLine(strokeColor, Offset(right, bottom - cornerLen), Offset(right, bottom), strokeW)
        drawLine(strokeColor, Offset(right - cornerLen, bottom), Offset(right, bottom), strokeW)
    }
}
