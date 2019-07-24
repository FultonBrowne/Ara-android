package com.andromeda.ara

import java.net.URL

class GetFood {
    fun nearMe(city: String) : RssFeedModel{
        var main :RssFeedModel = RssFeedModel("", "", "","")
        // TODO work on this
        khttp.get(
                url = "http://httpbin.org/get",
                params = mapOf("key1" to "value1", "keyn" to "valuen"))
        return main

    }
    fun search(term: String) : RssFeedModel{
        var main :RssFeedModel = RssFeedModel("", "", "","")
        return main
    }

}