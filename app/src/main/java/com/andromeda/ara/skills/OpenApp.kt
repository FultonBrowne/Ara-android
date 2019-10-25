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

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.Toast


class OpenApp {
    fun openApp(appName:String, ctx:Context){
        val apps = ctx.packageManager.getInstalledPackages(0)
        var returnedApp = ""
        for(i in apps){
           if(i.applicationInfo.name == appName){
               returnedApp = i.packageName;

           }
        }
        val intent:Intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        val cn = ComponentName("com.android.settings", returnedApp);
        intent.component = cn;
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK;
        try
        {
            ctx.startActivity(intent)
        }catch(e: ActivityNotFoundException){
            Toast.makeText(ctx,"Activity Not Found", Toast.LENGTH_SHORT).show()
        }

    }
}

