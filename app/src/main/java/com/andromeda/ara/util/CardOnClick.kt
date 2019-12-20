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
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import com.andromeda.ara.activitys.SkillsActivity
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.RunActions


class CardOnClick {
    fun mainFun(mode: Long, linkText: String, act: Activity, ctx: Context) {
        println("link text")
        if (mode == DrawerModeConstants.SHORTCUTS.toLong()) {
            try {
                val parsed = Parse().parse(linkText)
                val doIt = RunActions().doIt(parsed, "", ctx, act)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))
            act.startActivity(browserIntent)
        }

    }
    fun longClick(selected:RssFeedModel, ctx: Context, cursor: TagManager, mode: Long,act: Activity){
        if (mode == DrawerModeConstants.SHORTCUTS.toLong()) {
            val i = Intent(ctx, SkillsActivity::class.java)
            print(selected.link)
            i.putExtra("linktext", selected.link)
            act.startActivity(i)

        }
        insert(selected.title, selected.link, cursor)
        Toast.makeText(ctx, "Tagged", Toast.LENGTH_SHORT).show()
    }
    fun insert(main: String?, link: String?, main53: TagManager) {
        main53.open()
        main53.insert(main, link)
        main53.close()
    }

}