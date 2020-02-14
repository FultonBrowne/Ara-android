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

package com.andromeda.ara.util

import android.content.Context
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.andromeda.ara.constants.ServerUrl
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class GetSettings {
    fun starUp(ctx: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
        Crashes.setEnabled(prefs.getBoolean("getData", false))
        Analytics.setEnabled(prefs.getBoolean("getData", false))
        if (prefs.getBoolean("useOther", false)){
            ServerUrl.url = prefs.getString("serverText", "")!!

            Toast.makeText(ctx, ServerUrl.url, Toast.LENGTH_LONG).show()
        }
        System.err.println(ServerUrl.url + "hello")
    }
}