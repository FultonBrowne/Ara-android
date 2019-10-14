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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.andromeda.ara.R;
import com.andromeda.ara.activitys.MainActivity;

public class VoiceMain extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 13;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityCompat.requestPermissions(VoiceMain.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_voice_main);
        Context ctx = this;
        String toSpeak = "hello, I am ara";
        new TTS().start(getApplicationContext(), toSpeak);

        //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
        String search = new DeepSpeech().run( "/storage/self/primary/Download/main.mp3");
        Toast.makeText(getApplicationContext(), search,Toast.LENGTH_SHORT).show();


        //String phrase = new run().run1(ctx, this);
        //Toast.makeText(ctx, phrase, Toast.LENGTH_LONG).show();
        //TODO get lat and log
        //ArrayList<RssFeedModel> toFeed = new Search().main(phrase, "0", "0");




    }

    public void back(View view) {
        onBackPressed();

    }
    private void requestMicrophonePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                    new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);

        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(VoiceMain.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
