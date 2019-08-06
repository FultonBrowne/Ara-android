package com.andromeda.ara

import com.andromeda.ara.util.locl

class search {
    fun main(mainval: String, mode:Int): ArrayList<RssFeedModel>{
        var main1 : ArrayList<RssFeedModel> = java.util.ArrayList()
        main1.add(RssFeedModel("","","",""))
        if (mode == 1){
            main1.clear()
            main1.add(Wolfram().Wolfram1(mainval))
        }
       else if (mode == 2){
            main1.clear()
            main1 = food().searchfood(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else if (mode == 3){
            main1.clear()
            main1 = shopping().SearchShops(locl.longitude.toString(), locl.latitude.toString(), mainval)
        }
        else {
            main1.add(0, RssFeedModel(mainval,"","",""))
        }
        return main1
    }
}