/*
 * Copyright (c) 2019. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.search


import com.andromeda.ara.util.JsonParse
import com.andromeda.ara.util.OutputModel
import java.net.URL
import java.util.*

/**
 * AraSearch class, returns a json parse of url.
 */

class AraSearch {

    fun arrayOfOutputModels(search: String, log:String, lat:String): ArrayList<OutputModel>? {
        //get URL

            val url = URL("https://araserver.herokuapp.com/api/$search&log=$log&lat=$lat")

        println(url)
        var text = "[{\"title\":\"Blank Input Received\",\"link\":\"https://github.com/fultonbrowne/ara-android\",\"description\":\"Please Try Again\",\"OutputTxt\":\"Error Was Encountered\",\"exes\":\"\"}]"
        //parse Json
        try {
            text = url.readText()

        }
        catch (e:Exception){

        }

        return JsonParse().search(text)


    }


}