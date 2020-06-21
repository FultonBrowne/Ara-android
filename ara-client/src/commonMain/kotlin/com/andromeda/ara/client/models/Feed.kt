package com.andromeda.araserver.util
import com.andromeda.araserver.skills.SkillsModel

data class Feed(val type:String, val action:ArrayList<SkillsModel>?, val voice:String?, val feed:ArrayList<FeedModel>)
