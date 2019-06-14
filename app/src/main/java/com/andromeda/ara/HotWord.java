package com.andromeda.ara;


import android.content.Intent;
import android.os.Build;
import android.service.voice.AlwaysOnHotwordDetector;
import android.service.voice.VoiceInteractionService;
import android.util.Log;
import android.app.IntentService;


import androidx.annotation.RequiresApi;

import java.util.Locale;



@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class HotWord extends VoiceInteractionService {
    static final String TAG = "MainInteractionService";

            private final AlwaysOnHotwordDetector.Callback mHotwordCallback = new AlwaysOnHotwordDetector.Callback() {
        @Override
        public void onAvailabilityChanged(int status) {
                        Log.i(TAG, "onAvailabilityChanged(" + status + ")");
                        hotwordAvailabilityChangeHelper(status);
                    }

                @Override
        public void onDetected(AlwaysOnHotwordDetector.EventPayload eventPayload) {
                        Log.i(TAG, "onDetected");
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(), MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);



                }

                @Override
        public void onError() {
                        Log.i(TAG, "onError");
                    }

                @Override
        public void onRecognitionPaused() {
                        Log.i(TAG, "onRecognitionPaused");
                   }

                @Override
        public void onRecognitionResumed() {
                        Log.i(TAG, "onRecognitionResumed");
                   }
    };

            private AlwaysOnHotwordDetector mHotwordDetector;
    @Override
    public void onReady(){
        super.onReady();


               mHotwordDetector = createAlwaysOnHotwordDetector(
                               "Hey Ara", Locale.forLanguageTag("en-US"), mHotwordCallback);
           }

    private void hotwordAvailabilityChangeHelper(int availability) {
        Log.i(TAG, "Hotword availability = " + availability);
        switch (availability) {
            case AlwaysOnHotwordDetector.STATE_HARDWARE_UNAVAILABLE:
                Log.i(TAG, "STATE_HARDWARE_UNAVAILABLE");
                break;
            case AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNSUPPORTED:
                Log.i(TAG, "STATE_KEYPHRASE_UNSUPPORTED");
                break;
            case AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED:
                Log.i(TAG, "STATE_KEYPHRASE_UNENROLLED");
                Intent enroll = mHotwordDetector.createEnrollIntent();
                Log.i(TAG, "Need to enroll with " + enroll);
                break;
            case AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED:
                Log.i(TAG, "STATE_KEYPHRASE_ENROLLED - starting recognition");
                if (mHotwordDetector.startRecognition(
                        0)) {
                    Log.i(TAG, "startRecognition succeeded");
                } else {
                    Log.i(TAG, "startRecognition failed");
                }
                break;
        }
    }





}
