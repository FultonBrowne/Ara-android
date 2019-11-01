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
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.widget.Toast

import androidx.core.app.ActivityCompat

import com.andromeda.ara.R
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.util.*

import java.io.IOException
import java.util.ArrayList


class Drawer {
    @Throws(IOException::class)
    fun main(drawerItem: Long, ctx: Context, Db: TagManager, activity: Activity): MutableList<RssFeedModel> {
        var rssFeedModel1: MutableList<RssFeedModel> = ArrayList()
        var lat:Double = 0.0
        var log: Double = 0.0
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            println("running location system")

        val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                log = location.longitude
                lat = location.latitude
            }

        }



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
            rssFeedModel1 = Food().getFood(log.toString(), java.lang.Double.toString(lat))
        } else if (drawerItem == 4L) {
            rssFeedModel1 = shopping().getShops(java.lang.Double.toString(log), java.lang.Double.toString(lat))
        }
        else if (drawerItem == 5L) {


            rssFeedModel1 = CalUtility.readCalendarEvent(ctx)
        }
        else if (drawerItem == 6L){
            rssFeedModel1 = ApiOutputToRssFeed().main(Skills().getThem())
        }
        else if (drawerItem == 7L){
            rssFeedModel1 = Devices().getAll(activity)
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
        return rssFeedModel1
    }
}
