package com.andromeda.ara.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.andromeda.ara.RssFeedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class calUtility {
    public static String nameOfEvent;
    public static String startDates;
    public static String endDates;
    public static String descriptions;
    public static ArrayList<RssFeedModel>  main = new ArrayList<RssFeedModel>();

    public static ArrayList<RssFeedModel> readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[]{"calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation"}, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        main.clear();

        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent = cursor.getString(1);
            startDates =(getDate(Long.parseLong(cursor.getString(3))));
            endDates = (getDate(Long.parseLong(cursor.getString(4))));
            descriptions = (cursor.getString(2));
            main.add(new RssFeedModel(nameOfEvent, "", startDates + endDates + System.lineSeparator() + descriptions, ""));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();


        }
        return main;
    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
