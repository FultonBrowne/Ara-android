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

package com.andromeda.ara.skills

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import com.andromeda.ara.util.OnDeviceSkills
import com.andromeda.ara.util.OnDeviceSkillsDB
import com.andromeda.ara.util.RssFeedModel
import java.util.ArrayList

class UserSkills (c: Context?){
    private var dbHelper: UserSkillsDB? = null

    private var context: Context? =c

    private var database: SQLiteDatabase? = null



    @Throws(SQLException::class)
    fun open(): UserSkills? {
        dbHelper = UserSkillsDB(context!!)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(pre: String?, name: String?, act: String?) {
        val contentValue = ContentValues()
        contentValue.put(UserSkillsDB.PRE, pre)
        contentValue.put(UserSkillsDB.ACT, act)
        contentValue.put(UserSkillsDB.NAME, name)
        contentValue.put(UserSkillsDB.ID, Math.random())
        database!!.insertWithOnConflict(UserSkillsDB.TABLE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_REPLACE)
    }


    fun fetch(): Cursor? {
        val columns = arrayOf(UserSkillsDB.ID, UserSkillsDB.PRE, UserSkillsDB.ACT)
        val cursor = database!!.query(UserSkillsDB.TABLE_NAME, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        return cursor
    }

    fun getAsRssFeedModel(): ArrayList<RssFeedModel> {
        val cursor = fetch()
        val toReturn = ArrayList<RssFeedModel>()
        if (cursor != null) {
            while (!cursor.isAfterLast){
                toReturn.add(RssFeedModel("TODO", cursor.getString(1), cursor.getString(0), "", "", true ))
                cursor.moveToNext()
            }

        }
        return toReturn
    }
    fun fromId(id:Int): String {
        val cursor = fetch()
        var toReturn = ""
        if (cursor != null) {
            while (!cursor.isAfterLast){
                if (id == cursor.getInt(0)){
                    toReturn = cursor.getString(2)
                    break
                }
                    cursor.moveToNext()
            }
        }
        return toReturn
    }

}