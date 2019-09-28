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

package com.andromeda.ara;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class tagManager {

    private tagged dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public tagManager(Context c) {
        context = c;
    }

    public tagManager open() throws SQLException {
        dbHelper = new tagged(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String name, String desc) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(tagged.SUBJECT, name);
        contentValue.put(tagged.DESC, desc);
        database.insert(tagged.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { tagged._ID, tagged.SUBJECT, tagged.DESC };
        Cursor cursor = database.query(tagged.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String name, String desc) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(tagged.SUBJECT, name);
        contentValues.put(tagged.DESC, desc);
        int i = database.update(tagged.TABLE_NAME, contentValues, tagged._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(tagged.TABLE_NAME, tagged._ID + "=" + _id, null);
    }

}
