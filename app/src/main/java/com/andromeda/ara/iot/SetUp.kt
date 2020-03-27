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

package com.andromeda.ara.iot

import android.app.Activity
import android.widget.Toast
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.User
import com.andromeda.ara.models.HaModel
import com.andromeda.ara.util.AraPopUps
import com.google.gson.Gson

class SetUp {
    fun setUp(key:String, Url:String, act:Activity){
        try {
            deleteFromCloud()
        }
        catch (e:Exception){
            //an exception here should be ignored for new users With no DB entry
            e.printStackTrace()
        }

        var url = Url
        if(url.endsWith("/")) url = url.removeSuffix("/")
        if (!url.endsWith("/api")) url = "$url/api"
        if (!url.startsWith("http:") && !url.startsWith("https:")) url = "http://$url"
        val sharedPreferences = act.getSharedPreferences("iot", 0)
        val edit = sharedPreferences.edit()
        println(url)
        try {
            writeToCloud(url, key)
        }
        catch (e:Exception){
            Toast.makeText(act, "Fail", Toast.LENGTH_LONG).show()
        }
        edit.putString("url", url)
        edit.putString("key", key)
        edit.apply()
        CacheData().main(act)
        IotRequest.testPing()
    }
    fun writeToCloud(link:String, key: String){
        AraPopUps().newDoc(Gson().toJson(HaModel(link, key)), "ha-${User.id}")
    }
    fun deleteFromCloud(){
        "${ServerUrl.url}/del/user=${User.id}&id=ha-${User.id}"

    }
}