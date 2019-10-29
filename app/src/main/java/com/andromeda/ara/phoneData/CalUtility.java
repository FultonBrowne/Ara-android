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

package com.andromeda.ara.phoneData;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.andromeda.ara.R;
import com.andromeda.ara.util.RssFeedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CalUtility {
    public static ArrayList<RssFeedModel> main = new ArrayList<>();

    public static ArrayList<RssFeedModel> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{context.getString(R.string.calender_id), context.getString(R.string.title), context.getString(R.string.description),
                                context.getString(R.string.dtstart), context.getString(R.string.dtend), context.getString(R.string.eventLocation)}, null,
                        null, null);
        assert cursor != null;
        cursor.moveToFirst();
        // fetching calendars name
        @SuppressWarnings("MismatchedReadAndWriteOfArray") String[] CNames = new String[cursor.getCount()];

        // fetching calendars id
        main.clear();

        for (int i = 0; i < CNames.length; i++) {

            String nameOfEvent = cursor.getString(1);
            String startDates = (getDate(Long.parseLong(cursor.getString(3))));
            String endDates = (getDate(Long.parseLong(cursor.getString(4))));
            String descriptions = (cursor.getString(2));
            main.add(new RssFeedModel(nameOfEvent, "", startDates + endDates + System.lineSeparator() + descriptions, "", ""));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();


        }
        cursor.close();
        return main;
    }

    private static String getDate(long milliSeconds) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
