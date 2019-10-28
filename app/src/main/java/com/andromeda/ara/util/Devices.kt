/*
 * Copyright (c) 2019. Fulton Browne
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

import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import java.util.*

class Devices {
    fun getAll(): ArrayList<RssFeedModel> {
        val returnVal = ArrayList<RssFeedModel>()
        val toBeParsed = Data.list(DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
        for(i in toBeParsed.get().currentPage.items){
            i.id
            println(i.id)

        }
        returnVal.add(RssFeedModel("nothing the here", "", "", "", ""))
        return returnVal
    }
    fun addOne(toAdd: DeviceModel){
        Data.create(UUID.randomUUID().toString(), toAdd, DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
    }
}