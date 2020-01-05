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


import android.app.Activity
import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.andromeda.ara.constants.ServerUrl.url
import com.andromeda.ara.constants.User.id
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import java.net.URL
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties


class AraPopUps {
    fun newSkill(ctx: Context): String {
        val mapper = ObjectMapper(YAMLFactory())
        val toYML = ArrayList<SkillsModel>()
        toYML.add(SkillsModel("CALL", "", ""))
        var text = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ -> text = input.text.toString()
            try {
                val i = (Math.random() * (30000 + 1)).toInt()
                Data.create(i.toString(), SkillsDBModel(SkillsModel(mapper.writeValueAsString(toYML), "", ""), text), SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS)
            } catch (e: JsonProcessingException) {
                e.printStackTrace()
            }
        }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

        builder.show()
        return text
    }
    fun renameSkill(ctx: Context, id:String, allData: SkillsDBModel) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val text = input.text.toString()
            allData.name = text
            Data.replace(id, allData, SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS)
        }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }
    fun newDevice(ctx: Context, act:Activity){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val i = (Math.random() * (30000 + 1)).toInt()
            Data.create(i .toString(), DeviceModel(input.text.toString(), "LIGHT", "---\\- \\\"on\\\": true\\n  powerLevel: null\\n  color: null\\n\"", ""), DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
            val url = URL("${url}newdevice/user=${id}&id=$i")
            val deviceKey = GetUrlAra().getIt(url)
            NfcTransmit().main("", ctx, act)

        }
        builder.show()

    }
    fun editDevice(class1:Any, ctx: Context){
        val lin = LinearLayout(ctx)
        val class2 = class1::class as KClass<Any>
        class2.memberProperties.forEach {
            println(it.name)
        }

    }

}