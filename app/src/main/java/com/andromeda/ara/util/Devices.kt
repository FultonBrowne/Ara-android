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

package com.andromeda.ara.util

import android.app.Activity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import java.util.*

class Devices {
    fun getAll(activity: Activity): ArrayList<RssFeedModel> {
        var done = false
        val returnVal = ArrayList<RssFeedModel>()
        //addOne()

        returnVal.add(RssFeedModel("nothing the here", "", "", "", "", true))
        //returnVal.add(RssFeedModel(toBeParsed.get().currentPage.items[0].deserializedValue.name, "","", "", "", true))
        return returnVal
    }
    fun addOne(){
        val mapper = ObjectMapper(YAMLFactory())
        val toYml =ArrayList<LightStatusModel>()
        val i = (Math.random() * (30000 + 1)).toInt()
        toYml.add(LightStatusModel(true, null, null))
        Data.create(i.toString(), DeviceModel("test1234567890", "LIGHT",mapper.writeValueAsString(toYml), "" ),  DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
    }

    fun changeStatus(toSwitch: DeviceModel, id: String) {

    }
}