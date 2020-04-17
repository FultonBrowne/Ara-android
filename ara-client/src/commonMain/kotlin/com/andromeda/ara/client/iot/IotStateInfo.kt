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

import com.andromeda.ara.client.models.IotState
import com.andromeda.ara.client.models.RawIotState
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.serializer
import kotlin.reflect.KCallable
import kotlin.reflect.KMutableProperty


object IotStateInfo {
        const val OFF = 0
        const val ON = 1
        const val PLAY = 2
        const val PAUSE = 3
        const val SKIP_FWD = 4
        const val SKIP_BACK = 5
        fun fromHaOutput(stateAll:IotState): ArrayList<Int> {
            val state = stateAll.state
            val arrayList = arrayListOf<Int>()
            when {
                state.equals("on") -> arrayList.add(OFF)
                state.equals("off") -> arrayList.add(ON)
                state.equals("play") -> {
                    arrayList.add(PAUSE)
                    arrayList.addAll(skip())
                }
                state.equals("pause") -> {
                    arrayList.add(PLAY)
                    arrayList.addAll(skip())
                }
            }
            return arrayList


        }
    fun onPressed(id:String, newState:String, oldState:IotState){
        oldState.state = newState
        onPressed(id, oldState)
    }
    fun onPressed(id:String, newState:Int,  oldState:IotState){
        oldState.state = ""
        onPressed(id, oldState)
    }
    fun onPressed(id:String,  newState:IotState){
        val attributes = Any::class
        val attributesMap = mutableMapOf<String, JsonElement>()
        newState.attributes?.forEach {
            attributesMap[it.key] = JsonPrimitive(it.value)
        }
        val attributesObject = JsonObject(attributesMap)
        val jsonObject = JsonObject(mapOf("state" to JsonPrimitive(newState.state), "attributes" to attributesObject))
        println(jsonObject.content)

    }



    private fun skip(): ArrayList<Int> {
            return arrayListOf(
                SKIP_FWD,
                SKIP_BACK
            )
        }


}