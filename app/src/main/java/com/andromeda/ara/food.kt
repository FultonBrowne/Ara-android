package com.andromeda.ara

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse

import java.io.IOException
import java.util.HashMap

import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList

class food {
    fun getFood(log: String, lat: String): ArrayList<RssFeedModel> {
        var rssFeedModel1: ArrayList<RssFeedModel> = ArrayList()
        val main = RssFeedModel("", "", "", "")
        val apiFactory = YelpFusionApiFactory()
        try {
            val params = HashMap<String, String>()
            val yelpFusionApi = apiFactory.createAPI("TysaYZNEZB4aK1JhLAMT0BCeG0sdxDffcoFmnFnsEcVN5U9d4YA3UeRnrrw0FovvCsmWIalZQwexcPAqecflXv51tAXEtctkOgrdD3CIUculH7ieskJc6fKTguo4XXYx")
            params["latitude"] = lat
            params["longitude"] = log

            val call = yelpFusionApi.getBusinessSearch(params)
            val response = call.execute()
            val count = response.body().total
            var count2 = 1
            var title:String = "err"
            var info:String = "err"
            var web:String = "err"
            var image:String = "err"
            if (response.isSuccessful && count2 >= count){
            while (count <= count2){
               title = response.body().businesses[count2].name
                web = response.body().businesses[count2].url
                image = response.body().businesses[count2].imageUrl
                rssFeedModel1.add(RssFeedModel(title,web,info,image))
               // count2 + 1
            }



            }
            else {
                rssFeedModel1.add(RssFeedModel("err","err","err","err"))
            }


        } catch (e: IOException) {
            e.printStackTrace()
            rssFeedModel1.add(RssFeedModel("err","err","err","err"))
        }


        // general params


        return rssFeedModel1
    }

}
