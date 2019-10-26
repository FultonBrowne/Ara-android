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

package com.andromeda.ara.feeds

import android.Manifest
import android.app.Activity
import android.content.Context
import android.widget.Toast

import androidx.core.app.ActivityCompat

import com.andromeda.ara.R
import com.andromeda.ara.util.Adapter
import com.andromeda.ara.util.RssFeedModel
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.util.Locl
import com.andromeda.ara.util.TagManager

import java.io.IOException
import java.util.ArrayList


class Drawer {
    @Throws(IOException::class)
    fun main(drawerItem: Long, ctx: Context, Db: TagManager, activity: Activity): Adapter {
        var rssFeedModel1: MutableList<RssFeedModel> = ArrayList()


        if (drawerItem == 1L) {
            Toast.makeText(ctx, ctx.getString(R.string.number_1), Toast.LENGTH_SHORT).show()

            rssFeedModel1.addAll(Rss().parseRss(0))

        } else if (drawerItem == 2L) {
            var title1: String
            var web1: String
            Db.open()
            val cursor = Db.fetch()
            var test: RssFeedModel


            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst()


                while (!cursor.isAfterLast) {

                    title1 = cursor.getString(1)
                    web1 = cursor.getString(2)
                    test = RssFeedModel(title1, web1, "", "", "")

                    rssFeedModel1.add(test)
                    cursor.moveToNext()
                }
            } else {
                title1 = "nothing"
                web1 = "reload app"
                test = RssFeedModel(title1, web1, "", "", "")
                rssFeedModel1.add(test)


            }

        } else if (drawerItem == 3L) {
            rssFeedModel1 = Food().getFood(java.lang.Double.toString(Locl.longitude), java.lang.Double.toString(Locl.latitude))
        } else if (drawerItem == 4L) {
            Locl(ctx)
            rssFeedModel1 = shopping().getShops(java.lang.Double.toString(Locl.longitude), java.lang.Double.toString(Locl.latitude))
        }
        if (drawerItem == 5L) {

            ActivityCompat.requestPermissions(activity,
                    arrayOf(Manifest.permission.READ_CALENDAR),
                    1)
            rssFeedModel1 = CalUtility.readCalendarEvent(ctx)
        }
        if (drawerItem == 104L) {
            rssFeedModel1.addAll(Rss().parseRss(3))
        } else if (drawerItem == 102L) {
            rssFeedModel1.addAll(Rss().parseRss(2))
        }
        if (drawerItem == 103L) {
            rssFeedModel1.addAll(Rss().parseRss(1))
        }

        if (drawerItem == 105L) {
            rssFeedModel1.addAll(Rss().parseRss(4))
        }
        return Adapter(rssFeedModel1)
    }
}
