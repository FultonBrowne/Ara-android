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

package com.andromeda.ara.feeds;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.andromeda.ara.R;
import com.andromeda.ara.util.Adapter;
import com.andromeda.ara.util.RssFeedModel;
import com.andromeda.ara.util.CalUtility;
import com.andromeda.ara.util.Locl;
import com.andromeda.ara.util.TagManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Drawer {
  public Adapter main(long drawerItem, Context ctx, TagManager Db, Activity activity) throws IOException {
      List<RssFeedModel> rssFeedModel1 = new ArrayList<>();


      if (drawerItem == 1) {
            Toast.makeText(ctx, ctx.getString(R.string.number_1), Toast.LENGTH_SHORT).show();

                rssFeedModel1.addAll( new Rss().parseRss(0));

        } else if (drawerItem == 2) {
          String title1;
          String web1;
            Db.open();
            Cursor cursor = Db.fetch();
            RssFeedModel test;


            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();


                while (!cursor.isAfterLast()) {

                    title1 = cursor.getString(1);
                    web1 = cursor.getString(2);
                    test = new RssFeedModel(title1, web1, "", "", "");

                    rssFeedModel1.add(test);
                    cursor.moveToNext();
                }
            } else {
                title1 = "nothing";
                web1 = "reload app";
                test = new RssFeedModel(title1, web1, "", "", "");
                rssFeedModel1.add(test);


            }

        } else if (drawerItem == 3) {
            rssFeedModel1 = new Food().getFood(Double.toString(Locl.longitude), Double.toString(Locl.latitude));
        } else if (drawerItem == 4) {
            new Locl(ctx);
          rssFeedModel1 = new shopping().getShops(Double.toString((Locl.longitude)), Double.toString((Locl.latitude)));
        }
      if (drawerItem == 5) {

          ActivityCompat.requestPermissions(activity,
                  new String[]{Manifest.permission.READ_CALENDAR},
                  1);
          rssFeedModel1 = CalUtility.readCalendarEvent(ctx);
      }
          if (drawerItem == 104) {
              rssFeedModel1.addAll(new Rss().parseRss(3));
          }
      else if (drawerItem == 102) {
          rssFeedModel1.addAll(new Rss().parseRss(2));
      }
        if (drawerItem == 103) {
                rssFeedModel1.addAll(new Rss().parseRss(1));
            }

        if (drawerItem == 105) {
                rssFeedModel1.addAll(new Rss().parseRss(4));
    }
        return new Adapter(rssFeedModel1);
    }
}
