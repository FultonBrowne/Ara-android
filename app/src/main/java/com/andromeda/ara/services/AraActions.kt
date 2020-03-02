/*
 * Copyright (c) 2020. Fulton Browne
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

package com.andromeda.ara.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.andromeda.ara.R
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class AraActions : Service() {
    companion object{
        var timerRunning = false
        const val TIMER = 1

    }

    override fun onBind(intent: Intent): IBinder? {

        return null
    }

    override fun onStart(intent: Intent?, startId: Int) {
        val type = intent!!.getIntExtra("type", 0)
        if (type == TIMER) {
            timer(intent)

        }
    }

    private fun timer(intent: Intent) {
        val length = intent.getIntExtra("length", 1000)
        val timer = Timer()
        val ctx = this
        val channelId = "com.andromeda.ara"
        var builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_foreground)
                .setContentTitle("timer done")
                .setContentText("all done")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
        val task: TimerTask = object : TimerTask() {
            override fun run() {
                with(NotificationManagerCompat.from(ctx)) {
                    // notificationId is a unique int for each notification that you must define
                    timerRunning = false
                    notify(ThreadLocalRandom.current().nextInt(0, 10000000 + 1), builder.build())
                }

            }
        }
        timer.schedule(task, length.toLong())
        timerRunning = true
        stopSelf()
    }
}
