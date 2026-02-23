package com.sakhidev.fer.di

import android.content.Context
import com.sakhidev.fer.ml.MediaPipeFaceDetector
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt dependency injection module.
 * Provides the single source of truth for face detection and blendshape analytics.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideMediaPipeFaceDetector(
        @ApplicationContext context: Context
    ): MediaPipeFaceDetector = MediaPipeFaceDetector(context)
}
