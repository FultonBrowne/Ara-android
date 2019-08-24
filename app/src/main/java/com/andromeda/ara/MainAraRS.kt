/*
 * Copyright (c) 2019. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
