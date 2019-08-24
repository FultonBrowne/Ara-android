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

package com.andromeda.ara;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.service.voice.VoiceInteractionSession;
import android.view.View;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.M)
public class VoiceUi extends VoiceInteractionSession

        //implements View.OnClickListener
{
    View mContentView;


    public VoiceUi(Context context) {
        super(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ActivityManager am = getContext().getSystemService(ActivityManager.class);
        am.setWatchHeapLimit(40 * 1024 * 1024);
    }

    @Override
    public View onCreateContentView() {
        mContentView = getLayoutInflater().inflate(R.layout.voiceuitest, null);
        return mContentView;


    }
}
