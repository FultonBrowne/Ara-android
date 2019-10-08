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

import android.service.voice.VoiceInteractionService;
import android.util.Log;

import com.andromeda.ara.R;

//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AssistServ extends VoiceInteractionService {
    @Override
    public void onReady() {
        super.onReady();

        Log.v(getString(R.string.tag_AssistantService), getString(R.string.msg_onReady));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(getString(R.string.tag_AssistantService), getString(R.string.msg_onCreate));
    }
}
