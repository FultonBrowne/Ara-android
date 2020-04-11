
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

package com.andromeda.ara.feeds

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.reminders.Reminders
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.skills.ListSkills
import com.andromeda.ara.util.ApiOutputToRssFeed
import com.andromeda.ara.util.JsonParse
import com.andromeda.ara.util.SetFeedData
import com.andromeda.ara.util.TagManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL


class Drawer {
    @Throws(IOException::class)
    fun main(drawerItem: Long, ctx: Context, Db: TagManager, feedModel1: ArrayList<FeedModel>, setFeedData: SetFeedData): ArrayList<FeedModel>? {
        var lat = 0.0
        var log = 0.0
        feedModel1.clear()
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            println("running location system")

            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                log = location.longitude
                lat = location.latitude
            }

        }



        when (drawerItem) {
            DrawerModeConstants.HOME -> {
                feedModel1.addAll(News().newsGeneral(ctx, setFeedData))
                return feedModel1
            }
            DrawerModeConstants.TAGS -> {
                var title1: String
                var web1: String
                Db.open()
                val cursor = Db.fetch()
                var test: FeedModel


                if (cursor != null && cursor.moveToFirst()) {
                    cursor.moveToFirst()


                    while (!cursor.isAfterLast) {

                        title1 = cursor.getString(1)
                        web1 = cursor.getString(2)
                        test = FeedModel(title1, web1, "", "", "", false)

                        feedModel1.add(test)
                        cursor.moveToNext()
                    }
                } else {
                    title1 = "nothing"
                    web1 = "reload app"
                    test = FeedModel(title1, web1, "", "", "", false)
                    feedModel1.add(test)


                }
                return feedModel1

            }
            DrawerModeConstants.FOOD -> {
                GlobalScope.launch {
                    setFeedData.setData(Food().getFood(log.toString(), lat.toString()))
                }
                return null
            }
            DrawerModeConstants.SHORTCUTS -> {
                GlobalScope.launch {
                    setFeedData.setData(ListSkills().main())
                }
                return null
            }
            DrawerModeConstants.CAL -> {
                feedModel1.addAll( CalUtility.readCalendarEvent(ctx))
                return feedModel1
            }
            DrawerModeConstants.REMINDERS ->{
                GlobalScope.launch {
                    setFeedData.setData(Reminders().get())
                }
                return null
            }
            104L -> {
                feedModel1.addAll(News().newsGeneral(setFeedData))
                return feedModel1
            }
            102L -> {
                feedModel1.addAll(News().newsTech(setFeedData))
                return feedModel1
            }
            105L -> {
                feedModel1.addAll(News().newsMoney(setFeedData))
                return feedModel1
            }
            else -> {
                println("returning")
                return null

            }
        }

    }
}
