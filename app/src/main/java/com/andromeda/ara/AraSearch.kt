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

package com.andromeda.ara


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URL
import java.util.*


class AraSearch {

    val gson = Gson()
    fun arrayOfOutputModels(search: String): ArrayList<OutputModel>? {
        val url = URL("https://araserver.herokuapp.com/api/$search")
        System.out.println("Staring...")
        val returedtxt = url.readText()
        System.out.println(returedtxt)

        // val boxSearchCollection = gson.fromJson(returedtxt, OutputModel().javaClass )
        //val fooType = object : TypeToken<Foo<OutputModel>>() {

        // }.type
        val foo = Foo<OutputModel>()
        val fooType = object : TypeToken<Foo<OutputModel>>() {

        }.type
        gson.toJson(foo, fooType)

        return gson.fromJson(returedtxt, ArrayList<OutputModel>()::class.java) as ArrayList<OutputModel>



    }

    internal inner class Foo<T> {
        var value: T? = null
    }


}