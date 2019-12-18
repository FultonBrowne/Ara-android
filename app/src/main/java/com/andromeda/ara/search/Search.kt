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

package com.andromeda.ara.search

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.andromeda.ara.R
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.RunActions
import com.andromeda.ara.util.ApiOutputToRssFeed
import com.andromeda.ara.util.RssFeedModel
import java.net.InetAddress
import java.util.*


class Search {
    fun main(mainval: String, ctx: Context, act: Activity): ArrayList<RssFeedModel> {

        var outputList: ArrayList<RssFeedModel> = ArrayList()
        var local: List<String>? = null
        var lat = 0.0
        var log = 0.0
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                lat = location.latitude
                log = location.longitude
            }

            local = SkillsSearch().search(mainval, ctx)


        }
        if (local?.get(0) != "" && mainval != "" && local != null) {
            val parsed = Parse().parse(local?.get(0))
            val doIt = RunActions().doIt(parsed, mainval.replace((local.get(1)) + " ", ""), ctx, act)
            outputList.addAll(doIt)

        } else {

            outputList.add(RssFeedModel("", "", "", "", "", false))
            //search ara server
            var searchMode1 = mainval.toLowerCase(Locale("en"))
            searchMode1 = searchMode1.replace(" ", "%20")
            val test1 = AraSearch().arrayOfOutputModels(searchMode1, log.toString(), lat.toString())
            outputList = ApiOutputToRssFeed().main(test1)
            println(R.string.done_search)
            try {
                val parsed = Parse().parse(test1?.get(0)?.exes)
                val doIt = RunActions().doIt(parsed, mainval, ctx, act)
                outputList.addAll(doIt)
            } catch (e: Exception) {
            }
        }
        return outputList
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            val ipAddr = InetAddress.getByName("https://ara-server.azurewebsites.net/api").toString()
            //You can replace it with your name
            ipAddr != ""
        } catch (e: Exception) {
            false
        }
    }


}
