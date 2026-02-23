package com.sakhidev.fer.viewmodel

import android.graphics.RectF
import androidx.camera.core.ImageAnalysis
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sakhidev.fer.camera.FrameAnalyzer
import com.sakhidev.fer.ml.MediaPipeFaceDetector
import com.sakhidev.fer.model.CameraUiState
import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.model.EyeState
import com.sakhidev.fer.model.FaceData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Camera Screen.
 * Owns the FrameAnalyzer and exposes UI state via StateFlow.
 */
@HiltViewModel
class CameraViewModel @Inject constructor(
    private val faceDetector: MediaPipeFaceDetector
) : ViewModel() {

    private val _uiState = MutableStateFlow<CameraUiState>(CameraUiState.Idle)
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    /** Expose the FrameAnalyzer for CameraManager to use as ImageAnalysis.Analyzer */
    val frameAnalyzer: ImageAnalysis.Analyzer = FrameAnalyzer(
        faceDetector = faceDetector,
        onResult = { emotion, eyeState, landmarks, boundingBox, width, height ->
            handleDetectionResult(emotion, eyeState, landmarks, boundingBox, width, height)
        }
    )

    private fun handleDetectionResult(
        emotion: EmotionResult,
        eyeState: EyeState,
        landmarks: List<com.google.mediapipe.tasks.components.containers.NormalizedLandmark>,
        boundingBox: RectF?,
        width: Int,
        height: Int
    ) {
        viewModelScope.launch {
            if (boundingBox == null) {
                _uiState.value = CameraUiState.NoFaceDetected
            } else {
                _uiState.value = CameraUiState.FaceDetected(
                    FaceData(
                        boundingBox = boundingBox,
                        landmarks = landmarks,
                        emotion = emotion,
                        eyeState = eyeState,
                        imageWidth = width,
                        imageHeight = height
                    )
                )
            }
        }
    }

    fun setError(message: String) {
        _uiState.value = CameraUiState.Error(message)
    }

    fun resetState() {
        _uiState.value = CameraUiState.Idle
    }

    override fun onCleared() {
        super.onCleared()
        faceDetector.close()
    }
}
