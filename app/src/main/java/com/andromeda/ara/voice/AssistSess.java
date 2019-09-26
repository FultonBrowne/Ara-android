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

import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.service.voice.VoiceInteractionSession;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.PopupMenu;



//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AssistSess extends VoiceInteractionSession  {
    Context context;

    public AssistSess(Context context) {
        super(context);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onHandleAssist(Bundle data, AssistStructure structure, AssistContent content) {
        super.onHandleAssist(data, structure, content);
        Log.v("AssistantSession","onHandleAssist");
        Toast.makeText(context, "hi", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onHandleScreenshot(@Nullable Bitmap screenshot) {
        Log.v("AssistantSession","onHandleScreenshot");
        super.onHandleScreenshot(screenshot);
    }
}
