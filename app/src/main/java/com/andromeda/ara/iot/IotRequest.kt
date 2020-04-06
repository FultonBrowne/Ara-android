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

import com.andromeda.ara.client.models.FeedModel
import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.concurrent.thread

object IotRequest {
    fun baseRequestGet(path:String): Request {
        return Request.Builder().addHeader("Authorization", "Bearer ${IotCache.id}").method("GET", null).url(IotCache.url + path).build()
    }
    fun baseRequestPost(path:String, body:String): Request {
        return Request.Builder().addHeader("Authorization", "Bearer ${IotCache.id}").method("POST",body.toRequestBody()).url(IotCache.url + path).build()
    }
    fun testPing(){
        thread {
            try {
                println(client.newCall(baseRequestGet("/")).execute().body!!.string())
            }
            catch (e:Exception){
                e.printStackTrace()

            }
        }
    }
    fun parseAllAsFeed(): ArrayList<FeedModel> {
        val toReturn = arrayListOf<FeedModel>()
        try {
            val text = client.newCall(baseRequestGet("/states")).execute().body!!.string()
             JsonParser().parse(text).asJsonArray.forEach {
                try {
                    val jsonObject = it.asJsonObject
                    val attributes = jsonObject.get("attributes").asJsonObject
                    val name = attributes.get("friendly_name").asString
                    val id = jsonObject.get("entity_id").asString
                    val description = jsonObject.get("state").asString
                    toReturn.add(FeedModel(description, id, name, "", "", false))

                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }
        return toReturn
    }
    val client = OkHttpClient().newBuilder()
            .build()

}