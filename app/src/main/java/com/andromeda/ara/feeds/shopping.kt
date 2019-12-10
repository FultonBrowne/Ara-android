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

import com.andromeda.ara.util.RssFeedModel
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import java.io.IOException
import java.util.*

class shopping {
    /**
     * GetShops method , fetches data from yelp.
     */
    fun getShops(log: String, lat: String): ArrayList<RssFeedModel> {
        val rssFeedModel1: ArrayList<RssFeedModel> = ArrayList()
        val main = RssFeedModel("", "", "", "","", false)
        val apiFactory = YelpFusionApiFactory()
        try {
            val params = HashMap<String, String>()
            val yelpFusionApi = apiFactory.createAPI("TysaYZNEZB4aK1JhLAMT0BCeG0sdxDffcoFmnFnsEcVN5U9d4YA3UeRnrrw0FovvCsmWIalZQwexcPAqecflXv51tAXEtctkOgrdD3CIUculH7ieskJc6fKTguo4XXYx")
            params["latitude"] = lat
            params["longitude"] = log
            params["categories"] = "shopping"

            val call = yelpFusionApi.getBusinessSearch(params)
            val response = call.execute()
            val count = response.body().total
            val count2 = 1
            var title: String
            var info: String
            var web: String
            var image: String
            if (count2 <= count) {
                // rssFeedModel1.add(ArrayList)
                for (i in 0 until response.body().businesses.size) {
                    title = response.body().businesses[i].name
                    web = response.body().businesses[i].url
                    image = response.body().businesses[i].imageUrl
                    val stars = response.body().businesses[i].rating
                    val open = response.body().businesses[i].isClosed
                    val stars1 = "$stars stars"
                    if (open) {
                        info = stars1 + System.lineSeparator() + " closed now"
                    } else {
                        info = stars1 + System.lineSeparator() + " open now"
                    }

                    rssFeedModel1.add(RssFeedModel(info, web, title, image, "", true))
                }
            } else {
                rssFeedModel1.add(RssFeedModel("err1", "err", "err", "err","", false))
            }


        } catch (e: IOException) {
            e.printStackTrace()
            rssFeedModel1.add(RssFeedModel("err", "err", "err", "err","", false))
        }


        // general params


        return rssFeedModel1
    }

}
