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
import com.andromeda.ara.feeds.Tagged;

public class TagManager {

    private Tagged dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public TagManager(Context c) {
        context = c;
    }

    public TagManager open() throws SQLException {
        dbHelper = new Tagged(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(Tagged.Companion.getSUBJECT(), name);
        contentValue.put(Tagged.Companion.getDESC(), desc);
        database.insert(Tagged.Companion.getTABLE_NAME(), null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[]{Tagged.Companion.get_ID(), Tagged.Companion.getSUBJECT(), Tagged.Companion.getDESC()};
        Cursor cursor = database.query(Tagged.Companion.getTABLE_NAME(), columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tagged.Companion.getSUBJECT(), name);
        contentValues.put(Tagged.Companion.getDESC(), desc);
        int i = database.update(Tagged.Companion.getTABLE_NAME(), contentValues, Tagged.Companion.get_ID() + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(Tagged.Companion.getTABLE_NAME(), Tagged.Companion.get_ID() + "=" + _id, null);
    }

}
