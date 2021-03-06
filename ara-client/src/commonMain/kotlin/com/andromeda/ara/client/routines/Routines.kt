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

package com.andromeda.ara.client.routines

import com.andromeda.ara.client.models.SkillsDBModel
import com.andromeda.ara.client.models.SkillsModel
import com.andromeda.ara.client.util.JsonParse
import com.andromeda.ara.client.util.ReadURL
import com.andromeda.ara.client.util.ServerUrl
import com.andromeda.ara.client.util.ServerUrl.url
import com.andromeda.ara.client.util.User
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class Routines {
    suspend fun get(id:String): ArrayList<SkillsDBModel> {
        val url = "${ServerUrl.url}1user/user=${User.id}&id=$id"
        val data = ReadURL().get(url)
        return JsonParse().userSkills(data)
    }
    @ImplicitReflectionSerializer
    suspend fun get(): ArrayList<SkillsDBModel> {
        val url = ServerUrl.url + "user/" + User.id
        val data = ReadURL().get(url)
        return JsonParse().userSkills(data)
    }
    fun rename(id:String, name:String){
        val url = "${url}updateuserdata/user=${User.id}id=$id&prop=name&newval=${name}"
        GlobalScope.launch {
            ReadURL().get(url)
        }
    }
    @ImplicitReflectionSerializer
    fun edit(new:SkillsModel, id:String){
        GlobalScope.launch {  ReadURL().post("${ServerUrl.url}postupdate/user=${User.id}&id=$id&prop=action", Json.toJson(new).toString())}
    }
    @OptIn(ImplicitReflectionSerializer::class)
    fun new(id:String, new:SkillsDBModel){
        val payload = toJson(new)
        println(payload)
        ReadURL().post("${ServerUrl.url}newdoc/user=${User.id}&id=$id&prop=action", payload)
    }
    fun toJson(new:SkillsDBModel): String {
        val jsonObject = JsonObject(
            mapOf(
                "action" to JsonPrimitive(new.action.action),
                "arg1" to JsonPrimitive(new.action.arg1),
                "arg2" to JsonPrimitive(new.action.arg2)
            )
        )
        return JsonObject(
            mapOf(
                "action" to jsonObject,
                "name" to JsonPrimitive(new.name),
                "index" to JsonPrimitive(new.index)
            )
        ).toString()
    }
    fun delete(id:String){}
}