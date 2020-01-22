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
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.andromeda.ara.activitys.SkillsActivity
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.constants.User
import com.andromeda.ara.devices.DeviceModel
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.RunActions
import com.andromeda.ara.skills.SearchFunctions
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions


class CardOnClick {
    fun mainFun(mode: Long, linkText: String, act: Activity, ctx: Context, searchFunctions: SearchFunctions) {
        when (mode) {
            DrawerModeConstants.SHORTCUTS-> {
                try {
                    Data.read(linkText, SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS).thenAccept {
                        val parsed = Parse().parse(it.deserializedValue.action.action)
                        RunActions().doIt(parsed, "", ctx, act, searchFunctions)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            DrawerModeConstants.DEVICES -> {
                Data.read(linkText, DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS).thenAccept {
                    println(it.deserializedValue.status)
                    val parsed = Parse().yamlArrayToObjectList(it.deserializedValue.status, Any::class.java)
                    AraPopUps().editDevice(parsed[0], ctx, act, it.id)
                }
            }
            else -> {

                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))
                act.startActivity(browserIntent)
            }
        }

    }
    fun longClick(selected:RssFeedModel, ctx: Context, cursor: TagManager, mode: Long,act: Activity){
        if (mode == DrawerModeConstants.SHORTCUTS.toLong()) {
            val i = Intent(ctx, SkillsActivity::class.java)
            print(selected.link)
            i.putExtra("linktext", selected.link)
            act.startActivity(i)

        }
        else if(mode== DrawerModeConstants.DEVICES){
            Data.read(selected.link, DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS).thenAccept {
                println(it.deserializedValue.status)
                val parsed = Parse().yamlArrayToObjectList(it.deserializedValue.status, Any::class.java)
                AraPopUps().editDevice(act, it.id, User.id + it.id)
            }

        }
        insert(selected.title, selected.link, cursor)
        Toast.makeText(ctx, "Tagged", Toast.LENGTH_SHORT).show()
    }
    private fun insert(main: String?, link: String?, main53: TagManager) {
        main53.open()
        main53.insert(main, link)
        main53.close()
    }

}