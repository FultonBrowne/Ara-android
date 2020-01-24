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
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.ServerUrl.url
import com.andromeda.ara.constants.User.id
import com.andromeda.ara.devices.DeviceAdapter
import com.andromeda.ara.devices.DeviceModel
import com.andromeda.ara.devices.FinalDevice
import com.andromeda.ara.search.Search
import com.andromeda.ara.skills.SearchFunctions
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import java.net.URL
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible


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
    private fun newDevice(ctx: Context, act: Activity, url: URL){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val i = (Math.random() * (30000 + 1)).toInt()
            Data.create(i .toString(), DeviceModel(input.text.toString(), "LIGHT", url.readText(), ""), DeviceModel::class.java, DefaultPartitions.USER_DOCUMENTS)
            val url1 = URL("${ServerUrl.url}newdevice/user=${id}&id=$i")
            val deviceKey = GetUrlAra().getIt(url1)

        }
        builder.show()

    }
    fun textSearchResponse(ctx: Context, title: String, act: Activity, searchFunctions: SearchFunctions, recyclerView: RecyclerView){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle(title)
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("ok") { _, _ ->
            try {
                recyclerView.adapter = Adapter(Search().outputPing(input.text.toString(), ctx, act, searchFunctions), act);
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        builder.show()

    }
    fun textSearchString(ctx: Context, title: String, act: Activity, searchFunctions: SearchFunctions, recyclerView: RecyclerView){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle(title)
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("ok") { _, _ ->
            try {
                recyclerView.adapter = Adapter(Search().outputPing(input.text.toString(), ctx, act, searchFunctions), act);
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }
        builder.show()

    }
    fun editDevice(class1:Any, ctx: Context, act:Activity, id: String){
        val lin = RecyclerView(ctx)
        val listForMain = ArrayList<FinalDevice>()
        val class2 = class1::class.java as Class<Any>
        for(it in class2.kotlin.memberProperties) {
            if(!it.isAccessible && it.name == "entries"){
            it.isAccessible = true
                println(it.returnType)
            val data = (it.get(class1) as MutableSet<*>)
                for(it in data){
                    try {
                        val mainval = (it as MutableMap.MutableEntry<*, *>)

                            listForMain.add(FinalDevice(mainval.key.toString(), mainval.value))
                        println(listForMain)

                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                break
            }

        }
        val builder: AlertDialog.Builder = AlertDialog.Builder(act)
        lin.layoutManager = LinearLayoutManager(ctx)
        lin.adapter = (DeviceAdapter(listForMain, ctx, id))
        builder.setView(lin)
        act.runOnUiThread{
        builder.show()
        }
        println(listForMain)


    }
    fun editDevice(ctx: Activity,  id: String, DeviceID:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Device id is $DeviceID")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Delete") { _, _ ->
            Data.delete(id, DefaultPartitions.USER_DOCUMENTS)
        }
        builder.show()

    }
    fun DeviceNewWithType(ctx: Activity){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = Spinner(ctx)
        val adapter = ArrayAdapter.createFromResource(ctx, R.array.aradefaultdevices, R.layout.textskills)
        input.adapter = adapter
        builder.setView(input)
        builder.setPositiveButton("next"){ dialogInterface: DialogInterface, i: Int ->
            val map = mapOf(0 to "LIGHT", 1 to "TEMP")
            val url = URL(url + "/class/" + map[input.selectedItemPosition])
            newDevice(ctx, ctx, url)
        }
        builder.show()

    }
    private var resultValue = "null"

    fun getDialogValueBack(context: Context?, m: String): String {
        val handler: Handler = object : Handler() {
            override fun handleMessage(mesg: Message?) {
                throw RuntimeException()
            }
        }
        val alert = AlertDialog.Builder(context)
        alert.setTitle(m)
        val textView =EditText(context)
        alert.setView(textView)
        alert.setPositiveButton("ok") { dialog, id ->
            resultValue = textView.text.toString()
            handler.sendMessage(handler.obtainMessage())
        }
        alert.show()
        try {
            Looper.loop()
        } catch (e: RuntimeException) {
        }
        return resultValue
    }

}