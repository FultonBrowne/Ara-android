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
        contentValue.put(OnDeviceSkillsDB.Companion.getPRE(), pre);
        contentValue.put(OnDeviceSkillsDB.Companion.getEND(), end);
        contentValue.put(OnDeviceSkillsDB.Companion.getACT(), act);

        database.insert(OnDeviceSkillsDB.Companion.getTABLE_NAME(), null, contentValue);
    }




}
