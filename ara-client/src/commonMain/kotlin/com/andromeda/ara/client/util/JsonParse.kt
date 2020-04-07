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

package com.andromeda.ara.client.util

import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.models.NewsData
import com.andromeda.ara.client.models.OutputModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.content
import kotlinx.serialization.parse

class JsonParse {
    @OptIn(UnstableDefault::class)
    @ImplicitReflectionSerializer
    fun outputModel(text:String): ArrayList<OutputModel> {
        val array = arrayListOf<OutputModel>()
        Json.parseJson(text).jsonArray.forEach {
            val jo = it.jsonObject
            array.add(OutputModel(jo.get("title")!!.content, jo.get("description")!!.content, jo.get("link")!!.content, jo.get("image")!!.content, jo.get("OutputTxt")!!.content, jo.get("exes")!!.content))
        }
                return array
    }
    @ImplicitReflectionSerializer
    fun newsData(text:String): ArrayList<NewsData> {
        val array = arrayListOf<NewsData>()
        Json.parseJson(text).jsonArray.forEach {
            val jo = it.jsonObject
            array.add(NewsData(jo["title"]!!.content,
                jo["info"]!!.content, "", jo["link"]!!.content, jo["pic"]!!.content ))
        }
        return array
    }
    @ImplicitReflectionSerializer
    fun newsAsFeed(text:String): ArrayList<FeedModel> {
        val feedData = arrayListOf<FeedModel>()
        val news = newsData(text)
        for (i in news) {
            feedData.add(FeedModel(i.info, i.link, i.title, i.pic, "", true))
        }
        return feedData
    }
}