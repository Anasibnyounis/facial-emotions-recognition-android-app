package com.sakhidev.fer

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class annotated with @HiltAndroidApp to enable Hilt DI.
 * Referenced in AndroidManifest.xml via android:name=".FerApplication"
 */
@HiltAndroidApp
class FerApplication : Application()
