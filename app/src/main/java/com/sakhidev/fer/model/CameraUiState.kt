package com.sakhidev.fer.model

sealed class CameraUiState {
    object Idle : CameraUiState()
    object NoFaceDetected : CameraUiState()
    data class FaceDetected(val faceData: FaceData) : CameraUiState()
    data class Error(val message: String) : CameraUiState()
}
