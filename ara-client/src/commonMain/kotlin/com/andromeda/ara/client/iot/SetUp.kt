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

package com.andromeda.ara.client.iot

import com.andromeda.ara.client.models.HaModel
import com.andromeda.ara.client.models.IotDataModel
import com.andromeda.ara.client.util.JsonParse
import com.andromeda.ara.client.util.ReadURL
import com.andromeda.ara.client.util.ServerUrl
import com.andromeda.ara.client.util.ServerUrl.url
import com.andromeda.ara.client.util.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.json.Json

class SetUp {
    @ImplicitReflectionSerializer
    fun writeToCloud(link:String, key: String){
        val id = "ha-${User.id}"
        ReadURL().post("${url}newdoc/user=${User.id}&id=$id", Json.toJson(HaModel(link, key)).toString())
    }
    fun deleteFromCloud(){
        GlobalScope.launch {
            ReadURL().get("${ServerUrl.url}/del/user=${User.id}&id=ha-${User.id}")
        }

    }
    suspend fun getFromCloud(): IotDataModel? {
        return try {
            val get = ReadURL().get("${ServerUrl.url}getha/")
            val iot = JsonParse().iot(get)
            IotData.accessKey = iot[0].key
            IotData.urlToApi = iot[0].url
            iot[0]
        } catch (e:Exception){
            println(e.message)
            null
        }
    }
}