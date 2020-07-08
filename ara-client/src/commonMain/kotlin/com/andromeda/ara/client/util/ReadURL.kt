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

package com.andromeda.ara.client.util

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.takeFrom
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ReadURL {
    suspend fun get(url:String): String {
       println(url)
        val client = HttpClient()
        return client.get(url)
    }
    fun post(url:String, payload:String) {
        val client = HttpClient()
        val httpRequestBuilder = HttpRequestBuilder()
            httpRequestBuilder.headers["data"] = payload
        httpRequestBuilder.url.takeFrom(url)
        println(httpRequestBuilder.url.buildString())
        GlobalScope.launch {
            client.post(httpRequestBuilder)
        }
    }
}
