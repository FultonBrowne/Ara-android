/*
 * Copyright (c) 2020. Fulton Browne
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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;

import androidx.preference.PreferenceManager;

import com.andromeda.ara.R;
import com.andromeda.ara.util.FeedDateParseModel;
import com.andromeda.ara.util.RssFeedModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalUtility {
    public static ArrayList<RssFeedModel> main = new ArrayList<>();
    public static ArrayList<FeedDateParseModel> complexDataMain = new ArrayList<>();

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
            main.add(new RssFeedModel(nameOfEvent, "", startDates + endDates + System.lineSeparator() + descriptions, "", "", true));
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

    public static ArrayList<FeedDateParseModel> getComplexData(Context context) {
        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(currentDate);
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{context.getString(R.string.calender_id), context.getString(R.string.title), context.getString(R.string.description),
                                context.getString(R.string.dtstart), context.getString(R.string.dtend), context.getString(R.string.eventLocation)}, null,
                        null, null);
        assert cursor != null;
        cursor.moveToFirst();
        @SuppressWarnings("MismatchedReadAndWriteOfArray") String[] CNames = new String[cursor.getCount()];

        complexDataMain.clear();
        for (int i = 0; i < CNames.length; i++) {

            String nameOfEvent = cursor.getString(1);
            String startDates = (getDate(Long.parseLong(cursor.getString(3))));
            Date startDatesAsTime = (new Date(Long.parseLong(cursor.getString(3))));
            System.out.println(startDatesAsTime);
            String endDates = (getDate(Long.parseLong(cursor.getString(4))));
            String descriptions = (cursor.getString(2));
            long ltime = currentDate.getTime() + 4 * 24 * 60 * 60 * 1000;
            Date today4 = new Date(ltime);
            long toSub;
            if (currentDate.getTime() < startDatesAsTime.getTime() && !startDatesAsTime.after(today4)) {
                toSub = startDatesAsTime.getTime() - currentDate.getTime();
                toSub = toSub * 2;
                startDatesAsTime = new Date(startDatesAsTime.getTime() - toSub);
                complexDataMain.add(new FeedDateParseModel(nameOfEvent, startDates + endDates + System.lineSeparator() + descriptions, "", "", "", startDatesAsTime));
            }
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();


        }
        cursor.close();


        return complexDataMain;
    }
    public ArrayList<RssFeedModel> getClosestEvents(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Date currentDate = new Date(System.currentTimeMillis());
        System.out.println(currentDate);
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{context.getString(R.string.calender_id), context.getString(R.string.title), context.getString(R.string.description),
                                context.getString(R.string.dtstart), context.getString(R.string.dtend), context.getString(R.string.eventLocation)}, null,
                        null, null);
        ArrayList<RssFeedModel> rssFeedModels = new ArrayList<>();
        assert cursor != null;
        cursor.moveToFirst();
        long ltime = currentDate.getTime() + 60 * 1000 * prefs.getInt("time", 60) ;
        Date today4 = new Date(ltime);
        @SuppressWarnings("MismatchedReadAndWriteOfArray") String[] CNames = new String[cursor.getCount()];

        for (int i = 0; i < CNames.length; i++) {

            String nameOfEvent = cursor.getString(1);
            String startDates = (getDate(Long.parseLong(cursor.getString(3))));
            Date startDatesAsTime = (new Date(Long.parseLong(cursor.getString(3))));
            System.out.println(startDatesAsTime);
            DateFormat df = DateFormat.getTimeInstance();
            String descriptions = (cursor.getString(2));
            long toSub;
            CNames[i] = cursor.getString(1);
            if(startDatesAsTime.getTime() < ltime && startDatesAsTime.getTime() > currentDate.getTime()){
                String format = df.format(startDatesAsTime);
                RssFeedModel e = new RssFeedModel("at: " + format.substring(0, format.length() - 3), "", nameOfEvent, "", "", false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    e.color = context.getColor(R.color.accent);
                }
                rssFeedModels.add(e);

            }
            cursor.moveToNext();



        }
        cursor.close();

        complexDataMain.clear();
        return rssFeedModels;
    }

}
