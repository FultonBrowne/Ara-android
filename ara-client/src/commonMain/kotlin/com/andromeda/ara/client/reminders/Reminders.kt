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

package com.andromeda.ara.client.reminders

import com.andromeda.ara.client.models.RemindersModel
import com.andromeda.ara.client.util.ReadURL
import com.andromeda.ara.client.util.ServerUrl
import com.andromeda.ara.client.util.ServerUrl.url
import com.andromeda.ara.client.util.User

class Reminders {
    suspend fun get(id:String){
        val reminderUrl = ServerUrl.getReminder(id)
        ReadURL().get(reminderUrl)
    }
    suspend fun get(){
        val remindersList = ServerUrl.getRemindersList("", "", "", "")
        ReadURL().get(remindersList)
    }
    suspend fun new(remindersModel: RemindersModel){
        val replace =
            "$url/remindernn/name=$remindersModel&user=${User.id}&time=${remindersModel.time}&info=${remindersModel.body}".replace(
                " ",
                "%20"
            )
        ReadURL().get(replace)
    }
    suspend fun delete(id: String){
        val replace =
            "$url/reminderd/name=&user=${User.id}&id=$id".replace(
                " ",
                "%20"
            )
        ReadURL().get(replace)

    }

    fun set(id: String, remindersModel: RemindersModel){

    }
}