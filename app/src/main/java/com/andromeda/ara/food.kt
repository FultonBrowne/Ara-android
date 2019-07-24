package com.andromeda.ara

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse

import java.io.IOException
import java.util.HashMap

import retrofit2.Call
import retrofit2.Response

class food {
    internal operator fun get(log: String, lat: String): RssFeedModel {
        val main = RssFeedModel("", "", "", "")
        val apiFactory = YelpFusionApiFactory()
        try {
            val params = HashMap<String, String>()
            val yelpFusionApi = apiFactory.createAPI("test")
            //params.put("term", "indian food");
            params["latitude"] = lat
            params["longitude"] = log

            val call = yelpFusionApi.getBusinessSearch(params)
            val response = call.execute()


        } catch (e: IOException) {
            e.printStackTrace()
        }


        // general params


        return main
    }

}
