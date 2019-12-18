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
import com.andromeda.ara.util.SkillsModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import java.util.*


class SkillsSearch {
    fun search(phrase: String, ctx: Context): List<String> {
        val mapper = ObjectMapper(YAMLFactory())
        print(mapper.writeValueAsString(SkillsModel("OPEN_APP", "TERM", "")))
        val insert = OnDeviceSkills(ctx).open()
        val yml = ArrayList<SkillsModel>()

        yml.add(SkillsModel("OPEN_APP", "TERM", ""))
        insert.insert("open", "app", mapper.writeValueAsString(yml))
        insert.insert("open the", "app", mapper.writeValueAsString(yml))
        yml.clear()
        yml.add(SkillsModel("CALL", "TERM", ""))
        yml.add(SkillsModel("OUTPUT", "calling", ""))
        insert.insert("call", "", mapper.writeValueAsString(yml))
        insert.insert("dial", "", mapper.writeValueAsString(yml))
        yml.clear()
        yml.add(SkillsModel("TEXT", "TERM", ""))
        insert.insert("send a text", "", mapper.writeValueAsString(yml))
        insert.insert("send a text to", "", mapper.writeValueAsString(yml))
        insert.insert("text", "", mapper.writeValueAsString(yml))


        val dB = OnDeviceSkills(ctx).open()
        val cursor = dB.fetch()
        var pre = ""
        var end = ""
        var act: String
        var finalAct = ""

        // Check the SDK version and whether the permission is already granted or not.
        // Check the SDK version and whether the permission is already granted or not.


        cursor.moveToFirst()



        while (!cursor.isAfterLast) {
            pre = cursor.getString(1)
            end = cursor.getString(2)
            act = cursor.getString(3)
            cursor.moveToNext()
            if (phrase.startsWith(pre, true)) {
                finalAct = act
                break
            }
        }




        return listOf(finalAct, pre, end)
    }

}