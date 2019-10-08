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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andromeda.ara.R;
import com.andromeda.ara.search.Search;
import com.andromeda.ara.util.RssFeedModel;

import java.util.ArrayList;

public class VoiceMain extends AppCompatActivity {
    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_main);
        Context ctx = this;
        requestMicrophonePermission();

        String phrase = new run().run1(ctx, this);
        Toast.makeText(ctx, phrase, Toast.LENGTH_LONG).show();
        //TODO get lat and log
        ArrayList<RssFeedModel> toFeed = new Search().main(phrase, "0", "0");
        new TTS().start(ctx, "hello");



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
}
