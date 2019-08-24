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

import com.andromeda.ara.util.locl

class search {
    fun main(mainval: String, mode:Int): ArrayList<RssFeedModel>{
        var main1 : ArrayList<RssFeedModel> = java.util.ArrayList()
        main1.add(RssFeedModel("","","",""))
        if (mode == 1){
            main1.clear()
            main1.add(Wolfram().Wolfram1(mainval))
        }
       else if (mode == 2){
            main1.clear()
            main1 = food().searchfood(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else if (mode == 3){
            main1.clear()
            main1 = shopping().SearchShops(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else {
            main1.add(0, RssFeedModel(mainval,"","",""))
        }
        return main1
    }
}