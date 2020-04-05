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

package com.andromeda.ara.client.util

import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.models.OutputModel

class ApiOutToFeed {
    fun main(tofeed: ArrayList<OutputModel>): ArrayList<FeedModel> {
        val feedModels: ArrayList<FeedModel> = arrayListOf()
        println(tofeed.size)
        // System.out.println(tofeed.get(1).link.toString());
        try {
            for (i in 0..feedModels.size) {
                println(i)
                val mInfo: String = tofeed.get(i).description
                val mTitle: String = tofeed.get(i).title
                println(mTitle)
                val mlink: String = tofeed.get(i).link
                val mOut: String = tofeed.get(i).OutputTxt
                val mPic: String = tofeed.get(i).image
                val mainModel =FeedModel(mInfo, mlink, mTitle, mPic, mOut, true)
                println(mOut)
                feedModels.add(mainModel)
            }
        } catch (ignored: Exception) {
            
        }
        return feedModels
    }
}