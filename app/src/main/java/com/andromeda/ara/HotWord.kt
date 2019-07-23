package com.andromeda.ara


import android.content.Intent
import android.os.Build
import android.service.voice.AlwaysOnHotwordDetector
import android.service.voice.VoiceInteractionService
import android.util.Log

import androidx.annotation.RequiresApi

import java.util.Locale


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class HotWord : VoiceInteractionService() {
    private var mHotwordDetector: AlwaysOnHotwordDetector? = null
    private val mHotwordCallback = object : AlwaysOnHotwordDetector.Callback() {
        override fun onAvailabilityChanged(status: Int) {
            Log.i(TAG, "onAvailabilityChanged($status)")
            hotwordAvailabilityChangeHelper(status)
        }

        override fun onDetected(eventPayload: AlwaysOnHotwordDetector.EventPayload) {
            Log.i(TAG, "onDetected")
            val i = Intent()
            i.setClass(applicationContext, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(i)


        }

        override fun onError() {
            Log.i(TAG, "onError")
        }

        override fun onRecognitionPaused() {
            Log.i(TAG, "onRecognitionPaused")
        }

        override fun onRecognitionResumed() {
            Log.i(TAG, "onRecognitionResumed")
        }
    }

    override fun onReady() {
        super.onReady()


        mHotwordDetector = createAlwaysOnHotwordDetector(
                "Hey Ara", Locale.forLanguageTag("en-US"), mHotwordCallback)
    }

    private fun hotwordAvailabilityChangeHelper(availability: Int) {
        Log.i(TAG, "Hotword availability = $availability")
        when (availability) {
            AlwaysOnHotwordDetector.STATE_HARDWARE_UNAVAILABLE -> Log.i(TAG, "STATE_HARDWARE_UNAVAILABLE")
            AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNSUPPORTED -> Log.i(TAG, "STATE_KEYPHRASE_UNSUPPORTED")
            AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED -> {
                Log.i(TAG, "STATE_KEYPHRASE_UNENROLLED")
                val enroll = mHotwordDetector!!.createEnrollIntent()
                Log.i(TAG, "Need to enroll with $enroll")
            }
            AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED -> {
                Log.i(TAG, "STATE_KEYPHRASE_ENROLLED - starting recognition")
                if (mHotwordDetector!!.startRecognition(
                                0)) {
                    Log.i(TAG, "startRecognition succeeded")
                } else {
                    Log.i(TAG, "startRecognition failed")
                }
            }
        }
    }

    companion object {
        internal val TAG = "MainInteractionService"
    }


}
