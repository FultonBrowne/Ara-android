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

import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.util.JsonParse
import com.andromeda.ara.util.RssFeedModel
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class News {
    fun newsGeneral(): ArrayList<RssFeedModel> {
        val news = JsonParse().news(URL(ServerUrl.url + linkMapGeneral(Locale.getDefault())).readText())
        val feedData = arrayListOf<RssFeedModel>()
        for (i in news){
            feedData.add(RssFeedModel(i.info, i.link, i.title, i.pic, "", true))
        }
        return feedData

    }
    fun linkMapGeneral(locale: Locale): String? {
        val map = mapOf(Locale.US to "news/us", Locale.UK to "news/")
        return map.getOrElse(locale, { return "news/us"})
    }
}