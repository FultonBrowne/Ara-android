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

import com.andromeda.ara.client.models.*
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ImplicitReflectionSerializer
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.json.content
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.parse

class JsonParse {
    @OptIn(UnstableDefault::class)
    @ImplicitReflectionSerializer
    fun outputModel(text:String): ArrayList<OutputModel> {
        val array = arrayListOf<OutputModel>()
        Json.parseJson(text).jsonArray.forEach {
            val jo = it.jsonObject
            array.add(OutputModel(jo.get("title")!!.content, jo.get("description")!!.content, jo.get("link")!!.content, jo.get("image")!!.content, jo["OutputTxt"]!!.content, jo.get("exes")!!.content))
        }
        println(array)
                return array
    }
    fun feed(text:String):Feed{
	    val obj  =  Json.parseJson(text).jsonObject
	    val action  = obj.getArrayOrNull("action")
	    val mainFeed = obj.getObject("data").getArray("feed")
	    val actionsArray = arrayListOf<SkillsModel>()
	    val feedArray = arrayListOf<FeedModel>()
	    action?.forEach{
		    val jo = it.jsonObject
		    actionsArray.add(SkillsModel(jo.get("action")!!.content,jo.get("arg1")!!.content, jo.get("arg2")!!.content))

	    }
	    mainFeed.forEach{
		    val jo = it.jsonObject
		    feedArray.add(FeedModel(jo.get("title")!!.content, jo.get("description")!!.content, jo.get("link")?.contentOrNull, jo.get("image")?.contentOrNull, jo.get("longText")!!.primitive.boolean, jo.get("color")?.primitive?.intOrNull))
	    }
	    return Feed(obj.getObject("data").get("type")!!.content, actionsArray, obj.get("voice")?.contentOrNull, feedArray)
    }
    fun reminder(text:String): RemindersModel {
        val array = arrayListOf<RemindersModel>()
        Json.parseJson(text).jsonArray.forEach {
            val jo = it.jsonObject
            return RemindersModel(jo.get("header")!!.content, jo.get("body")!!.contentOrNull,
                jo.get("time")!!.contentOrNull?.toLong()
            )
        }
        return RemindersModel("", "", 0)
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
    fun userSkills(text: String): ArrayList<SkillsDBModel> {
        val toReturn = arrayListOf<SkillsDBModel>()
        val parseJson = Json.parseJson(text).jsonArray
        parseJson.forEach {
            val main = it.jsonObject
            println(main)
            try {
                val sub = main.getObject("action")
                val model = SkillsDBModel(
                    SkillsModel(
                        sub.get("action")!!.content,
                        sub.get("arg1")!!.content,
                        sub.get("arg2")!!.content
                    ), main.get("name")!!.content, main.get("index")!!.content
                )
                toReturn.add(model)
            }
            catch (e:Exception){
                println(e)
            }
        }
        return toReturn
    }
    fun iot(text:String): ArrayList<IotDataModel> {
        val array = arrayListOf<IotDataModel>()
        Json.parseJson(text).jsonArray.forEach {
            val jo = it.jsonObject
            array.add(IotDataModel( jo["link"]!!.content, jo["key"]!!.content ))
        }
        return array
    }

}
