package com.andromeda.ara

import com.yelp.fusion.client.connection.YelpFusionApi
import com.yelp.fusion.client.connection.YelpFusionApiFactory
import com.yelp.fusion.client.models.SearchResponse

import java.io.IOException
import java.util.HashMap

import retrofit2.Call
import retrofit2.Response
import java.util.ArrayList
import android.R.attr.x
import java.nio.file.Files.size



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
            val count = response.body().total - 1
            var count2 = 1
            var title:String = "err"
            var info:String = "err"
            var web:String = "err"
            var image:String = "err"
            if (count2 <= count){
               // rssFeedModel1.add(ArrayList)
            for (i in 0  until response.body().businesses.size) {
                title=response.body().businesses[i].name
                web=response.body().businesses[i].url
                image=response.body().businesses[i].imageUrl

                rssFeedModel1.add(RssFeedModel(title,web,info,image))
                }
            }

            else {
                rssFeedModel1.add(RssFeedModel("err1","err","err","err"))
            }


        } catch (e: IOException) {
            e.printStackTrace()
            rssFeedModel1.add(RssFeedModel("err","err","err","err"))
        }


        // general params


        return rssFeedModel1
    }

}
