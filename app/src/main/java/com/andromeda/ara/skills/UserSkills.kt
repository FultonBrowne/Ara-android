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

class UserSkills (c: Context?){
    private var dbHelper: OnDeviceSkillsDB? = null

    private var context: Context? =c

    private var database: SQLiteDatabase? = null



    @Throws(SQLException::class)
    fun open(): UserSkills? {
        dbHelper = OnDeviceSkillsDB(context!!)
        database = dbHelper!!.writableDatabase
        return this
    }

    fun close() {
        dbHelper!!.close()
    }

    fun insert(pre: String?, end: String?, act: String?) {
        val contentValue = ContentValues()
        contentValue.put(UserSkillsDB.PRE, pre)
        contentValue.put(UserSkillsDB.ACT, act)
        database!!.insertWithOnConflict(OnDeviceSkillsDB.TABLE_NAME, null, contentValue, SQLiteDatabase.CONFLICT_REPLACE)
    }


    fun fetch(): Cursor? {
        val columns = arrayOf(UserSkillsDB.ID, UserSkillsDB.PRE, UserSkillsDB.ACT)
        val cursor = database!!.query(UserSkillsDB.TABLE_NAME, columns, null, null, null, null, null)
        cursor?.moveToFirst()
        return cursor
    }
}