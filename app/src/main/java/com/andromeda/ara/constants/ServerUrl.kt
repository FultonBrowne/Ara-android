/*
 * Copyright (c) 2020. Fulton Browne
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

package com.andromeda.ara.constants

import java.util.*

object ServerUrl {
    var url = "https://ara-server.azurewebsites.net/"
    fun getStandardSearch(term:String, log:String, lat:String): String {
        return "$url/api/${searchDataParser(term, log, lat)}"
    }
    fun getWebSearch(term:String, log:String, lat:String): String {
        return "$url/searchb/${searchDataParser(term, log, lat)}"
    }
    fun getImageSearch(term:String, log:String, lat:String): String {
        return "$url/searchi/${searchDataParser(term, log, lat)}"
    }
    fun getNewsSearch(term:String, log:String, lat:String): String {
        return "$url/searchn/${searchDataParser(term, log, lat)}"
    }
    fun getVideoSearch(term:String, log:String, lat:String): String {
        return "$url/searchv/${searchDataParser(term, log, lat)}"
    }
    private fun searchDataParser(term:String, log:String, lat:String): String {
        return "$term&log=$log&lat=$lat&cc=${Locale.getDefault().country}".replace(" ", "%20")
    }
}