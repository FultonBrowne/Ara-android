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

package com.andromeda.ara.voice;

import android.content.Context;
import android.content.Intent;
import android.service.voice.AlwaysOnHotwordDetector;
import android.service.voice.VoiceInteractionService;
import android.service.voice.VoiceInteractionSession;
import android.service.voice.VoiceInteractionSessionService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;

import com.andromeda.ara.R;

import java.util.Locale;

//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AssistServ extends VoiceInteractionService {
    private static final String TAG = "v";
    Context ctx;
    public AlwaysOnHotwordDetector mHotwordDetector;
    private AlwaysOnHotwordDetector.Callback mHotwordCallback = new AlwaysOnHotwordDetector.Callback() {


        @Override
        public void onAvailabilityChanged(int status) {
            Log.i(TAG, "onAvailabilityChanged($status)");
            hotwordAvailabilityChangeHelper(status);

        }

        @Override
        public void onDetected(@NonNull AlwaysOnHotwordDetector.EventPayload eventPayload) {

        }

        @Override
        public void onError() {

        }

        @Override
        public void onRecognitionPaused() {

        }

        @Override
        public void onRecognitionResumed() {

        }


    };
    @Override
    public void onReady() {
        super.onReady();ctx = this;

         mHotwordDetector = createAlwaysOnHotwordDetector(
                "hello ara", Locale.forLanguageTag("en-US"), mHotwordCallback);


        Log.v(getString(R.string.tag_AssistantService), getString(R.string.msg_onReady));
    }

    @Override
    public void onCreate() {
        //PreferenceManager preferenceManager = new PreferenceManager(getApplicationContext());

        super.onCreate();

        Log.v(getString(R.string.tag_AssistantService), getString(R.string.msg_onCreate));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
        private void hotwordAvailabilityChangeHelper(Integer availability) {
            Log.i(TAG, "Hotword availability = $availability");

            switch (availability) {
                case AlwaysOnHotwordDetector.STATE_HARDWARE_UNAVAILABLE :{ Log.i(TAG, "STATE_HARDWARE_UNAVAILABLE");
                break;}
                case AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNSUPPORTED : {Log.i(TAG, "STATE_KEYPHRASE_UNSUPPORTED");
                break;}
                case AlwaysOnHotwordDetector.STATE_KEYPHRASE_UNENROLLED : {
                    Log.i(TAG, "STATE_KEYPHRASE_UNENROLLED");

                        Intent enroll = mHotwordDetector.createEnrollIntent();
                        Log.i(TAG, "Need to enroll with " + enroll);


                }
                case AlwaysOnHotwordDetector.STATE_KEYPHRASE_ENROLLED :{
                    Log.i(TAG, "STATE_KEYPHRASE_ENROLLED - starting recognition");

                    if (mHotwordDetector.startRecognition(
                            0)) {
                        Log.i(TAG, "startRecognition succeeded");
                    } else {
                        Log.i(TAG, "startRecognition failed");
                    }
                }
                break;

            }
        }
}
