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
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class TTS {
    private TextToSpeech t1;

   public void start(Context ctx, String text) {
        t1 = new TextToSpeech(ctx, status -> {
            if (status != TextToSpeech.ERROR) {
                t1.setLanguage(Locale.UK);
                t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                System.out.println("done");
            }
            else System.out.println("errrrrrrrrr");
        });
    }

}
