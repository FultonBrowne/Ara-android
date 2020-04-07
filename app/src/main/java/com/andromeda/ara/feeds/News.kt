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

import android.content.Context
import com.andromeda.ara.client.feeds.News
import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.util.JsonParse
import com.andromeda.ara.util.NewsData
import com.andromeda.ara.util.SetFeedData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL
import java.util.*

class News {
    fun newsGeneral(setFeedData: SetFeedData): ArrayList<FeedModel> {
        val generalAsFeed = arrayListOf<FeedModel>()
        GlobalScope.launch {
            generalAsFeed.addAll(News().generalAsFeed(Locale.getDefault().country))
            setFeedData.setData(generalAsFeed)
        }
        return generalAsFeed


    }
    fun newsGeneral(ctx:Context, setFeedData: SetFeedData): ArrayList<FeedModel> {
        val feedData = newsGeneral(setFeedData)
        try {
            GlobalScope.launch {
                feedData.addAll(News().generalAsFeed(Locale.getDefault().country))
                feedData.addAll(
                    0,
                    CalUtility().getClosestEvents(ctx)
                )
                setFeedData.setData(feedData)
            }
        }
        catch (e:Exception){
            e.printStackTrace()
        }

        return feedData

    }
    private fun linkMapGeneral(locale: Locale): String? {
        val map = mapOf(Locale.US to "news/us", Locale.UK to "news/")
        return map.getOrElse(locale, { return "news/us"})
    }
    fun newsTech(): ArrayList<FeedModel> {
        return getFromLink(ServerUrl.url + "news/tech")

    }
    fun newsMoney(): ArrayList<FeedModel> {
        return getFromLink(ServerUrl.url + "news/money")

    }

    private fun getFromLink(link:String): ArrayList<FeedModel> {
        val news = JsonParse().news(URL(link).readText())
        val feedData = arrayListOf<FeedModel>()
        parse(news, feedData)
        return feedData
    }

    private fun parse(news: ArrayList<NewsData>, feedData: ArrayList<FeedModel>) {
        for (i in news) {
            feedData.add(FeedModel(i.info, i.link, i.title, i.pic, "", true))
        }
    }
}