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

package com.andromeda.ara.skills

import com.andromeda.ara.util.YamlModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.CollectionType
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import java.io.IOException


class Parse{
    fun parse(yml:String?): ArrayList<YamlModel>? {
        print(yml)
        var classsss:Class<YamlModel>? = YamlModel::class.java
        var mapper = YAMLMapper()

      return  YamlArrayToObjectList(yml, classsss)
    }



    @Throws(IOException::class)
    fun <T> YamlArrayToObjectList(yaml: String?, tClass: Class<T>?): ArrayList<T>? {
        //val mapper = ObjectMapper()
        val mapper = ObjectMapper(YAMLFactory()) // jackson databind


        val listType: CollectionType = mapper.typeFactory.constructCollectionType(ArrayList::class.java, tClass)

        return mapper.readValue(yaml, listType)
    }
}