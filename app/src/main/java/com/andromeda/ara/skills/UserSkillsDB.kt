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

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserSkillsDB(context: Context) : SQLiteOpenHelper(context, UserSkillsDB.DB_NAME, null, UserSkillsDB.DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {

        // Table Name
        const val TABLE_NAME = "USERSKILLS"

        // Table columns
        const val ID = "_id"
        const val PRE = "hotword"
        const val ACT = "action"
        const val NAME = "name"

        // Database Information
        private const val DB_NAME = "USERSKILLS.DB"

        // database version
        internal const val DB_VERSION = 1

        // Creating table query
        private const val CREATE_TABLE = ("create table " + TABLE_NAME + "(" + ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRE + " TEXT NOT NULL, " + NAME + " TEXT NOT NULL, "+ ACT + " TEXT);")

    }
}