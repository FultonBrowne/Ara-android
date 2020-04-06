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

package com.andromeda.ara.client.search

import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.models.SkillsModel
import com.andromeda.ara.client.util.ApiOutToFeed
import com.andromeda.ara.client.util.JsonParse
import com.andromeda.ara.client.util.ServerUrl
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.ImplicitReflectionSerializer

class SearchAra {
    @OptIn(ImplicitReflectionSerializer::class)
    suspend fun search(lat: String, log: String, term: String, locale: String, actions: Actions): ArrayList<FeedModel> {
        val client = HttpClient()

        val data = client.get<String>(ServerUrl.getStandardSearch(
                term = term,
                log = log,
                lat = lat,
                locale = locale
        ))
        client.close()
        val outputModel = JsonParse().outputModel(data)
        try {
           val yaml = actions.parseYaml<ArrayList<SkillsModel>>(outputModel[0].exes)
            actions.runActions(yaml, term)
        }
        catch (e:Exception){
            println(e.message)
        }
        return ApiOutToFeed().main(outputModel)


    }
}