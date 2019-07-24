package com.andromeda.ara

class search {
    fun main(mainval: String, mode:Int): RssFeedModel{
        var main1: RssFeedModel
        main1 = RssFeedModel("","","","")
        if (mode == 1){
            main1 =  Wolfram().Wolfram1(mainval)
        }
        if (mode == 2){
            main1 = GetFood().search(mainval)
        }
        return main1
    }
}