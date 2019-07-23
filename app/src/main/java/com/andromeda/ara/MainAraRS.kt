package com.andromeda.ara

import android.content.Intent
import android.speech.RecognitionService
import android.util.Log

/**
 * Stub recognition service needed to be a complete voice interactor.
 */
class MainAraRS : RecognitionService() {

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate")
    }

    override fun onStartListening(recognizerIntent: Intent, listener: RecognitionService.Callback) {
        Log.d(TAG, "onStartListening")
    }

    override fun onCancel(listener: RecognitionService.Callback) {
        Log.d(TAG, "onCancel")
    }

    override fun onStopListening(listener: RecognitionService.Callback) {
        Log.d(TAG, "onStopListening")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private val TAG = "MainRecognitionService"
    }
}
