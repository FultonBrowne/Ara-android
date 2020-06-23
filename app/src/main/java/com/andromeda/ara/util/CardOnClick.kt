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
import com.andromeda.ara.client.iot.Actions
import com.andromeda.ara.client.iot.GetNewInputs
import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.User
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.RunActions
import com.andromeda.ara.skills.SearchFunctions
import java.net.URL



class CardOnClick {
    fun mainFun(mode: Long, linkText: String, act: Activity, ctx: Context, searchFunctions: SearchFunctions, getNewInputs: GetNewInputs) {
        when (mode) {
            DrawerModeConstants.SHORTCUTS-> {
                try {
                    val parsed = Parse().parse(JsonParse().skillsServer(URL("${ServerUrl.url}1user/user=${User.id}&id=$linkText").readText())[0].action.action)
                    RunActions().doIt(parsed, "", ctx, act, searchFunctions)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
            DrawerModeConstants.DEVICES -> {
                    Actions().edit(linkText, getNewInputs)

            }
            DrawerModeConstants.REMINDERS ->{
                AraPopUps().editReminder(act, linkText)
            }
            else -> {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))
                act.startActivity(browserIntent)
            }
        }

    }
    fun longClick(selected: FeedModel, ctx: Context, cursor: TagManager, mode: Long, act: Activity){
        when (mode) {
            DrawerModeConstants.SHORTCUTS -> {
                val i = Intent(ctx, SkillsActivity::class.java)
                print(selected.link)
                i.putExtra("linktext", selected.link)
                act.startActivity(i)

            }
            DrawerModeConstants.REMINDERS -> {
                AraPopUps().delete(act, selected.link!!)
            }
            else -> {
                insert(selected.title, selected.link, cursor)
                Toast.makeText(ctx, "Tagged", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun insert(main: String?, link: String?, main53: TagManager) {
        main53.open()
        main53.insert(main, link)
        main53.close()
    }

}
