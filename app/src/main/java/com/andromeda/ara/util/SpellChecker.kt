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

package com.andromeda.ara.util

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class SpellChecker {
    var host = "https://api.cognitive.microsoft.com"
    var path = "/bing/v7.0/spellcheck"

    var key = "77216f36144140f7b408d7488478b827"

    var mkt = "en-US"
    var mode = "proof"
    @Throws(Exception::class)
    fun check(text:String): String? {
        val params = "?mkt=$mkt&mode=$mode"
        // add the rest of the code snippets here (except prettify() and main())...
        val url = URL(host + path + params)
        val connection = url.openConnection() as HttpsURLConnection
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Ocp-Apim-Subscription-Key", key);
        connection.setDoOutput(true);
        val wr = DataOutputStream(connection.outputStream)
        wr.writeBytes("text=$text")
        wr.flush()
        wr.close()
        val `in` = BufferedReader(
                InputStreamReader(connection.inputStream))
        var line: String?
        while (`in`.readLine().also { line = it } != null) {
          return prettify(line, text)
        }
        `in`.close()
        return null
    }
    fun prettify(json_text: String?, oldText:String): String? {
        val parser = JsonParser()
        var returnval = oldText
        val json: JsonElement = parser.parse(json_text)
        val json2 = json.asJsonObject.get("flaggedTokens").asJsonArray
        println(json_text)
        for (i in json2){
            returnval = returnval.replace(i.asJsonObject.get("token").asString, i.asJsonObject.get("suggestions").asJsonArray[0].asJsonObject.get("suggestions").asString )
        }
        return returnval
    }


}