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
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.startActivity


class OpenApp {
    fun openApp(appName:String, ctx:Context){
        val apps = ctx.packageManager.getInstalledApplications(0)
        print(apps)
        var returnedApp = ""

            val pm: PackageManager = ctx.packageManager
//get a list of installed apps.
            //get a list of installed apps.
            val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)

            for (packageInfo in packages) {

                if(packageInfo.loadLabel(pm).toString().toLowerCase() == appName){
                    returnedApp = packageInfo.packageName
                }
            }
        val launchIntent: Intent? = ctx.getPackageManager().getLaunchIntentForPackage(returnedApp)
        launchIntent?.let { ctx.startActivity(it) }
        }

    }


