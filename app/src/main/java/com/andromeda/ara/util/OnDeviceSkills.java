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

package com.andromeda.ara.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class OnDeviceSkills {
    private OnDeviceSkillsDB dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public OnDeviceSkills(Context c) {
        context = c;
    }

    public OnDeviceSkills open() throws SQLException {
        dbHelper = new OnDeviceSkillsDB(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String pre, String end, String act) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(OnDeviceSkillsDB.PRE, pre);
        contentValue.put(OnDeviceSkillsDB.END, end);
        contentValue.put(OnDeviceSkillsDB.ACT, act);
        database.insertWithOnConflict(OnDeviceSkillsDB.TABLE_NAME,null,contentValue,SQLiteDatabase.CONFLICT_REPLACE);

    }




    public Cursor fetch() {
        String[] columns = new String[]{OnDeviceSkillsDB.ID, OnDeviceSkillsDB.PRE, OnDeviceSkillsDB.END, OnDeviceSkillsDB.ACT};
        Cursor cursor = database.query(OnDeviceSkillsDB.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return  cursor;


        }
    }