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

package com.andromeda.ara.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.auth.Auth
import com.microsoft.appcenter.crashes.Crashes

class GetSettings {
    fun starUp(ctx:Context){
        val prefs = PreferenceManager.getDefaultSharedPreferences(ctx)
        Auth.setEnabled(prefs.getBoolean("araAccount", true))
        Crashes.setEnabled(prefs.getBoolean("getData", true))
        Analytics.setEnabled(prefs.getBoolean("getData", true))


    }
}