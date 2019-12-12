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
import android.net.Uri
import com.andromeda.ara.activitys.About
import com.andromeda.ara.activitys.SkillsActivity
import com.andromeda.ara.constants.DrawerModeConstants


class CardOnClick {
    fun mainFun(mode:Long, linkText:String, act:Activity, ctx:Context){
        if(mode == DrawerModeConstants.SHORTCUTS.toLong()){
            act.startActivity(Intent(ctx, SkillsActivity::class.java))

        }
        else{

            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(linkText))
                    act.startActivity(browserIntent)
        }

    }
}