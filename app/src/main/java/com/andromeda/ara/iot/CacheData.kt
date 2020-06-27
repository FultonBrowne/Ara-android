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
import com.andromeda.ara.client.iot.IotData
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.util.JsonParse
import java.net.URL

class CacheData {
    fun main(act:Activity){
        val sharedPreferences = act.getSharedPreferences("iot", 0)
        IotData.urlToApi = sharedPreferences.getString("url", "")!!
        IotData.accessKey= sharedPreferences.getString("key", "")!!
        try {
            //getFromCloud(act)
        }
        catch (e:Exception){
            e.printStackTrace()
        }
    }
    fun getFromCloud(act:Activity){
	Thread{
        val data = URL("${ServerUrl.url}getha/").readText()
        val parse = JsonParse().iot(data)
        val url = parse[0].link
        val key = parse[0].key
        val sharedPreferences = act.getSharedPreferences("iot", 0)
        val edit = sharedPreferences.edit()
        println(url)
        edit.putString("url", url)
        edit.putString("key", key)
        edit.apply()
}.start()
    }
}
