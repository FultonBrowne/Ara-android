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

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.andromeda.ara.R
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.widgets.WidgetConstants.ACTION_AUTO_UPDATE
import java.util.*


/**
 * Implementation of App Widget functionality.
 */
class TodayWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }
    override fun onReceive(context:Context, intent:Intent)
    {
        super.onReceive(context, intent);

        if(intent.getAction().equals(ACTION_AUTO_UPDATE))
        {
            // DO SOMETHING
            println("update")
            update(context, null, null )
        }

    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        // start alarm
        // start alarm
        val appWidgetAlarm = RefreshAlarm(context.applicationContext)
        appWidgetAlarm.startAlarm()
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        if (appWidgetIds.size == 0) { // stop alarm
            val appWidgetAlarm = RefreshAlarm(context.applicationContext)
            appWidgetAlarm.stopAlarm()
        }
    }
}

internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
    val appWidgetAlarm = RefreshAlarm(context.applicationContext)
    appWidgetAlarm.startAlarm()
    update(context, appWidgetManager, appWidgetId)
}

private fun update(context: Context, appWidgetManager: AppWidgetManager?, appWidgetId: Int?) {
    println("update")
    val instance = Calendar.getInstance()
    val dayOfWeek = instance.get(Calendar.DAY_OF_WEEK)
    val month = instance.get(Calendar.MONTH)
    val day = instance.get(Calendar.DAY_OF_MONTH)
    var dayTxt = ""
    val currentEvents = CalUtility().getClosestEvents(context)
    dayTxt = if (currentEvents.size == 0) "${WidgetConstants.week[dayOfWeek]}, ${WidgetConstants.month[month]} $day"
    else currentEvents[0].title + " " + currentEvents[0].description
    // Construct the RemoteViews object
    val views = RemoteViews(context.packageName, R.layout.today_widget)
    views.setTextViewText(R.id.appwidget_text, dayTxt)
    // Instruct the widget manager to update the widget
    if (appWidgetId != null) {
        appWidgetManager?.updateAppWidget(appWidgetId, views)
    }
}