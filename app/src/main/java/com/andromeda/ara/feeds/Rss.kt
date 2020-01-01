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
import android.os.Build
import androidx.annotation.RequiresApi
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.util.FeedDateParseModel
import com.andromeda.ara.util.RssFeedModel
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.io.IOException
import java.net.URL
import java.util.*

/**
 * RSS Class
 */
class Rss {
    @Throws(IOException::class)
    fun parseRss(mode: Int = 0, ctx: Context): List<RssFeedModel> {
        var mFeedTitle: String
        var mFeedLink: String
        var mFeedDescription: String
        var mFeedDate: Date


        var items = ArrayList<FeedDateParseModel>()
        var xmlReader: XmlReader? = null

        try {
            var feed = URL("https://ara-server.azurewebsites.net/")
            when (mode) {
                1 -> feed = URL("https://ara-server.azurewebsites.net/world")
                2 -> feed = URL("https://ara-server.azurewebsites.netm/tech")
                3 -> feed = URL("https://ara-server.azurewebsites.net/us")
                4 -> feed = URL("https://ara-server.azurewebsites.net/money")
            }


            feed.openConnection()
            xmlReader = XmlReader(feed)


            val feedAllData = SyndFeedInput().build(xmlReader)
            val iterator = feedAllData.entries.iterator()
            while (iterator.hasNext()) {
                val syndEntry = iterator.next() as SyndEntry

                mFeedDescription = syndEntry.description.value
                mFeedTitle = syndEntry.title
                mFeedLink = syndEntry.link
                mFeedDate = syndEntry.publishedDate


                val rssFeedModel = FeedDateParseModel(mFeedTitle, mFeedDescription, mFeedLink, "", "", mFeedDate)
                items.add(rssFeedModel)


            }
            if (mode == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                try {
                    items.addAll(CalUtility.getComplexData(ctx))
                    println("got the cal")
                    items = sortDate(items)

                } catch (e: IndexOutOfBoundsException) {
                    e.printStackTrace()
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
            mFeedLink = "err"
            mFeedTitle = "please connect"
            mFeedDescription = ""
            val rssFeedModel = FeedDateParseModel(mFeedDescription, mFeedLink, mFeedTitle, "", "", Date(0, 0, 0, 0, 0, 0))
            items.add(rssFeedModel)

        } catch (e: FeedException) {
            e.printStackTrace()
            mFeedLink = ""
            mFeedTitle = "feed error"
            mFeedDescription = "We will fix it, don't worry"
            val rssFeedModel = FeedDateParseModel(mFeedDescription, mFeedLink, mFeedTitle, "", "", Date(0, 0, 0, 0, 0, 0))
            items.add(rssFeedModel)

        } finally {
            xmlReader?.close()
        }
        val toReturn = ArrayList<RssFeedModel>()
        for (i in items) {
            toReturn.add(i.toRssFeedModel())
        }


        return toReturn


    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun sortDate(tosort: ArrayList<FeedDateParseModel>): ArrayList<FeedDateParseModel> { //sort by date
        tosort.sortWith(Comparator.comparing { obj: FeedDateParseModel -> obj.date })
        // return sorted value
        tosort.reverse()
        return tosort
    }
}