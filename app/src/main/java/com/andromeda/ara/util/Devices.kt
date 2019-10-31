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

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import java.util.*

class Devices {
    fun getAll(activity: Activity): ArrayList<RssFeedModel> {
        var done = false
        print("adding")
        print("added")
        val returnVal = ArrayList<RssFeedModel>()
        val toBeParsed = Data.list(DeviceModel::class.java
                , DefaultPartitions.USER_DOCUMENTS)
        print("getting ids")
        print("done")
        returnVal.add(RssFeedModel("nothing the here", "", "", "", ""))
        //returnVal.add(RssFeedModel(toBeParsed.get().currentPage.items[0].deserializedValue.name, "","", "", ""))
        activity.runOnUiThread {
                print(toBeParsed.isDone)


        }
        toBeParsed.get()
        return returnVal
    }
    fun addOne(toAdd: DeviceModel){
        Data.create(toAdd.id, toAdd, DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
    }
    fun changeStatus(toSwitch:DeviceModel, id:String){

    }
}