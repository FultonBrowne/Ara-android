package com.andromeda.araserver.util

/** 
This is the type used for the feed list
 **/
 data class FeedModel(var title: String, var description: String, var link: String?, var image: String?, var longText: Boolean, var color:Int?) {
	 constructor(title:String, description:String, link:String, image:String, longText:Boolean):this(title, description, link, image, longText, null)
	 constructor(title:String, description:String, link:String):this(title, description, link, null, false, null)
	 constructor(title:String, description:String):this(title, description, null , null, false, null)
}
