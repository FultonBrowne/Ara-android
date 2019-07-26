package com.andromeda.ara

class search {
    fun main(mainval: String, mode:Int): ArrayList<RssFeedModel>{
        var main1 : ArrayList<RssFeedModel> = java.util.ArrayList()
        main1.add(RssFeedModel("","","",""))
        if (mode == 1){
            main1.clear()
            main1.add(Wolfram().Wolfram1(mainval))
        }
        if (mode == 2){
            main1.clear()
            main1 = food().searchfood(locl.longitude.toString(),locl.latitude.toString(), mainval)
        }
        return main1
    }
}