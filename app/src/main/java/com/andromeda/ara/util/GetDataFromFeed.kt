package com.andromeda.ara.util
import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.models.Feed

class GetDataFromFeed{
	fun getFeedArray(feed:Feed):ArrayList<FeedModel>{
		return feed.feed
	}
}
