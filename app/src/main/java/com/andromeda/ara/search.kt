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

package com.andromeda.ara

import com.andromeda.ara.util.ApiOutputToRssFeed
import com.andromeda.ara.util.locl

class search {
    fun main(mainval: String, mode:Int): ArrayList<RssFeedModel>{
        var OutputList: ArrayList<RssFeedModel> = java.util.ArrayList()
        OutputList.add(RssFeedModel("", "", "", ""))
        if (mode == 1){
            var searchmode1 = mainval.toLowerCase()
            searchmode1.replace("", "_")
            /** OutputList.clear()


            if (searchmode1.startsWith("nearest")) {
            searchmode1 = searchmode1.padStart(8)
            OutputList = LocSearch().searchfood(locl.longitude.toString(), locl.latitude.toString(), searchmode1)
            }
            else OutputList.add(Wolfram().Wolfram1(mainval))**/
            val test1 = AraSearch().arrayOfOutputModels(searchmode1)
            OutputList = ApiOutputToRssFeed().main(test1)
            System.out.println("done")
            //System.out.println(OutputList[0].title)


        }
       else if (mode == 2){
            OutputList.clear()
            OutputList = food().searchfood(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else if (mode == 3){
            OutputList.clear()
            OutputList = shopping().SearchShops(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else {
            OutputList.add(0, RssFeedModel(mainval, "", "", ""))
        }
        return OutputList
    }
}