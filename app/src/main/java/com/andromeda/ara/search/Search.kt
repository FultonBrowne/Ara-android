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

import com.andromeda.ara.util.ApiOutputToRssFeed
import com.andromeda.ara.util.RssFeedModel

class Search {
    fun main(mainval: String, log:String,lat:String): ArrayList<RssFeedModel> {
        var outputList: ArrayList<RssFeedModel> = java.util.ArrayList()
        outputList.add(RssFeedModel("", "", "", ""))
        //search ara server
        var searchmode1 = mainval.toLowerCase()
        searchmode1 = searchmode1.replace(" ", "%20")
        val test1 = AraSearch().arrayOfOutputModels(searchmode1, log, lat)
        outputList = ApiOutputToRssFeed().main(test1)
        println("done")

        return outputList
    }
}