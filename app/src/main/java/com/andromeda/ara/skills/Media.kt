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

package com.andromeda.ara.skills

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import java.util.*


class Media {
    fun playPause(ctx: Context) {
        val mediaEvent = Intent(Intent.ACTION_MEDIA_BUTTON)
        val event = KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY)
        mediaEvent.putExtra(Intent.EXTRA_KEY_EVENT, event)
        ctx.sendBroadcast(mediaEvent)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val mediaEvent = Intent(Intent.ACTION_MEDIA_BUTTON)
                val event = KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY)
                mediaEvent.putExtra(Intent.EXTRA_KEY_EVENT, event)
                ctx.sendBroadcast(mediaEvent)
            }
        }, 100)

    }

    fun volumeUp() {

    }
}