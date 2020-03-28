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
import com.andromeda.ara.util.AraPopUps
import com.google.gson.JsonParser




class Edit {
    fun main(id: String, act: Activity){
        try {
            val newState: String
            val string = IotRequest.client.newCall(IotRequest.baseRequestGet("/states/$id")).execute().body!!.string()
            val parse = JsonParser().parse(string).asJsonObject
            val stringData = parse.get("state").asString
            newState = when (stringData) {
                "off" -> "on"
                "on" -> "off"
                else -> AraPopUps().getDialogValueBack(act, id)
            }
            parse.addProperty("state", newState)
            println(IotRequest.client.newCall(IotRequest.baseRequestPost("/states/$id", parse.toString())).execute().body!!.string())
            Toast.makeText(act, "state changed to $newState", Toast.LENGTH_LONG).show()
        }
        catch (e:Exception){
            e.printStackTrace()
            Toast.makeText(act, "fail", Toast.LENGTH_LONG).show()
        }
    }
}