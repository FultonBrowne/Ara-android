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
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.util.RssFeedModel
import com.andromeda.ara.util.TagManager
import java.io.IOException


class Drawer {
    @Throws(IOException::class)
    fun main(drawerItem: Long, ctx: Context, Db: TagManager, rssFeedModel1: ArrayList<RssFeedModel>): ArrayList<RssFeedModel>? {
        var lat: Double = 0.0
        var log: Double = 0.0
        rssFeedModel1.clear()
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
                rssFeedModel1.addAll(News().newsGeneral(ctx))
                return rssFeedModel1
            }
            DrawerModeConstants.TAGS -> {
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
                        test = RssFeedModel(title1, web1, "", "", "", false)

                        rssFeedModel1.add(test)
                        cursor.moveToNext()
                    }
                } else {
                    title1 = "nothing"
                    web1 = "reload app"
                    test = RssFeedModel(title1, web1, "", "", "", false)
                    rssFeedModel1.add(test)


                }
                return rssFeedModel1

            }
            DrawerModeConstants.FOOD -> {
                rssFeedModel1.addAll(Food().getFood(log.toString(), java.lang.Double.toString(lat)))
                return rssFeedModel1
            }
            DrawerModeConstants.CAL -> {
                rssFeedModel1.addAll( CalUtility.readCalendarEvent(ctx))
                return rssFeedModel1
            }
            104L -> {
                rssFeedModel1.addAll(News().newsGeneral())
                return rssFeedModel1
            }
            102L -> {
                rssFeedModel1.addAll(News().newsTech())
                return rssFeedModel1
            }
            105L -> {
                rssFeedModel1.addAll(News().newsMoney())
                return rssFeedModel1
            }
            else -> {
                println("returning")
                return null

            }
        }

    }
}
