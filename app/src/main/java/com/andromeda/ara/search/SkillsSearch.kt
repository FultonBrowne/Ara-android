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

package com.andromeda.ara.search

import android.content.Context
import com.andromeda.ara.util.OnDeviceSkills


class SkillsSearch {
    fun search(phrase:String, ctx:Context): List<String> {
        var DB = OnDeviceSkills(ctx).open()
        val cursor = DB.fetch()
        var pre:String =""
        var end:String = ""
        var act:String = ""
        var finalAct = ""

        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst()


            while (!cursor.isAfterLast) {

                pre = cursor.getString(1)
                end = cursor.getString(2)
                act = cursor.getString(3)
                cursor.moveToNext()
                if(phrase.startsWith(pre, true) || phrase.endsWith(end, true)){
                    finalAct = act
                    break

                }
            }
        } else {

        }
        return listOf<String>(finalAct, pre, end)
    }

}