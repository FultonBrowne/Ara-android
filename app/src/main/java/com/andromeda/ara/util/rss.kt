package com.andromeda.ara.util

import com.andromeda.ara.RssFeedModel
import com.rometools.rome.feed.synd.SyndEntry
import com.rometools.rome.io.FeedException
import com.rometools.rome.io.SyndFeedInput
import com.rometools.rome.io.XmlReader
import java.io.IOException
import java.net.URL
import java.util.*

class rss {
    @Throws(IOException::class)
    fun parseRss(): List<RssFeedModel> {
        var mFeedTitle: String
        var mFeedLink: String
        var mFeedDescription: String


        val items = ArrayList<RssFeedModel>()
        var xmlReader: XmlReader? = null

        try {
            val feed = URL("https://www.espn.com/espn/rss/news/rss.xml")



            feed.openConnection()
            xmlReader = XmlReader(feed)

            val input = SyndFeedInput()
            val feedAllData = SyndFeedInput().build(xmlReader)
            val iterator = feedAllData.entries.iterator()
            while (iterator
                            .hasNext()) {
                val syndEntry = iterator.next() as SyndEntry
                mFeedDescription = syndEntry.description.value
                mFeedTitle = syndEntry.title
                mFeedLink = syndEntry.link

                val rssFeedModel = RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "")
                items.add(rssFeedModel)


            }


        } catch (e: IOException) {
            mFeedLink = "err"
            mFeedTitle = "err"
            mFeedDescription = "err"
            val rssFeedModel = RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "")
            items.add(rssFeedModel)

        } catch (e: FeedException) {
            mFeedLink = "err"
            mFeedTitle = "err"
            mFeedDescription = "err"
            val rssFeedModel = RssFeedModel(mFeedDescription, mFeedLink, mFeedTitle, "")
            items.add(rssFeedModel)

        } finally {
            xmlReader?.close()
        }


        return items


    }
}