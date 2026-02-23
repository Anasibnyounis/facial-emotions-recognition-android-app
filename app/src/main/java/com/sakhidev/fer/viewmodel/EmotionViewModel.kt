package com.sakhidev.fer.viewmodel

import androidx.lifecycle.ViewModel
import com.sakhidev.fer.model.CameraUiState
import com.sakhidev.fer.model.EmotionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/**
 * ViewModel for emotion result display.
 * Can be observed independently from camera state for UI animations.
 */
@HiltViewModel
class EmotionViewModel @Inject constructor() : ViewModel() {

    private val _lastEmotion = MutableStateFlow(EmotionResult())
    val lastEmotion: StateFlow<EmotionResult> = _lastEmotion.asStateFlow()

    private val _detectionCount = MutableStateFlow(0)
    val detectionCount: StateFlow<Int> = _detectionCount.asStateFlow()

    fun updateEmotion(result: EmotionResult) {
        _lastEmotion.value = result
        _detectionCount.value += 1
    }

    fun clearEmotion() {
        _lastEmotion.value = EmotionResult()
    }
}
