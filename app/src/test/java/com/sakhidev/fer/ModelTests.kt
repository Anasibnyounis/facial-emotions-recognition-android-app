package com.sakhidev.fer

import com.sakhidev.fer.model.EmotionResult
import com.sakhidev.fer.model.EyeState
import org.junit.Assert.*
import org.junit.Test

class EmotionResultTest {

    @Test
    fun `emoji returns correct emoji for HAPPY`() {
        val result = EmotionResult(emotion = "HAPPY", confidence = 0.9f)
        assertEquals("😊", result.emoji)
    }

    @Test
    fun `emoji returns correct emoji for SAD`() {
        val result = EmotionResult(emotion = "SAD", confidence = 0.8f)
        assertEquals("😢", result.emoji)
    }

    @Test
    fun `confidencePercent converts correctly`() {
        val result = EmotionResult(emotion = "HAPPY", confidence = 0.876f)
        assertEquals(87, result.confidencePercent)
    }

    @Test
    fun `default emotion is NEUTRAL`() {
        val result = EmotionResult()
        assertEquals("NEUTRAL", result.emotion)
    }
}

class EyeStateTest {

    @Test
    fun `OPEN state returns correct label`() {
        assertEquals("EYES OPEN", EyeState.OPEN.label)
    }

    @Test
    fun `CLOSED state returns correct label`() {
        assertEquals("EYES CLOSED", EyeState.CLOSED.label)
    }

    @Test
    fun `OPEN state returns eye emoji`() {
        assertEquals("👁", EyeState.OPEN.emoji)
    }
}
