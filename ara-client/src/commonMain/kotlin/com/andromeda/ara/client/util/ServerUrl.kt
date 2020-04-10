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

@file:Suppress("SpellCheckingInspection")

package com.andromeda.ara.client.util


object ServerUrl {
    var url = "https://ara-server.azurewebsites.net/"
    fun getStandardSearch(term:String, log:String, lat:String, locale:String): String {
        return "$url/api/${searchDataParser(term, log, lat, locale)}"
    }

        fun getWebSearch(term:String, log:String, lat:String, locale:String): String {
        return "$url/searchb/${searchDataParser(term, log, lat, locale)}"
    }
    fun getImageSearch(term:String, log:String, lat:String, locale:String): String {
        return "$url/searchi/${searchDataParser(term, log, lat, locale)}"
    }
    fun getNewsSearch(term:String, log:String, lat:String,locale:String): String {
        return "$url/searchn/${searchDataParser(term, log, lat, locale)}"
    }
    fun getVideoSearch(term:String, log:String, lat:String, locale:String): String {
        return "$url/searchv/${searchDataParser(term, log, lat, locale)}"
    }
    fun getRemindersList(term:String, log:String, lat:String,locale:String): String {
        return "$url/remindergaapi/${searchDataParser(term, log, lat, locale)}"
    }
    private fun searchDataParser(term:String, log:String, lat:String, locale:String): String {
        return "$term&log=$log&lat=$lat&key=${User.id}".replace(" ", "%20")
    }
    fun getReminder(id:String): String {
        return "$url/reminderg/user=${User.id}&id=$id"
    }
}