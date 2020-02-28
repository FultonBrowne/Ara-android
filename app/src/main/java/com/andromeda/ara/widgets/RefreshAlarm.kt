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

package com.andromeda.ara.widgets

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*


class RefreshAlarm(context: Context?) {
    private val ALARM_ID = 0
    private val INTERVAL_MILLIS = 60000

    private var mContext: Context? = context


    fun startAlarm() {
        println("start")
        val calendar: Calendar = Calendar.getInstance()
        calendar.add(Calendar.MILLISECOND, INTERVAL_MILLIS)
        val alarmIntent = Intent(mContext, TodayWidget::class.java)
        alarmIntent.action = WidgetConstants.ACTION_AUTO_UPDATE
        val pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = mContext!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // RTC does not wake the device up
        alarmManager.setRepeating(AlarmManager.RTC, calendar.timeInMillis, INTERVAL_MILLIS.toLong(), pendingIntent)
        println("gooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
    }


    fun stopAlarm() {
        val alarmIntent = Intent(WidgetConstants.ACTION_AUTO_UPDATE)
        val pendingIntent = PendingIntent.getBroadcast(mContext, ALARM_ID, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager = mContext?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}