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


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.ServerUrl.url
import com.andromeda.ara.constants.User
import com.andromeda.ara.constants.User.id
import com.andromeda.ara.devices.DeviceAdapter
import com.andromeda.ara.devices.FinalDevice
import com.andromeda.ara.search.Search
import com.andromeda.ara.skills.SearchFunctions
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
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
                newDoc(Gson().toJson(SkillsDBModel(SkillsModel(mapper.writeValueAsString(toYML), "", ""), text, id)), i.toString() + User.id)
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
            URL("${url}updateuserdata/user=${User.id}id=20392&prop=name&newval=$text")
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
            Toast.makeText(ctx, "offline as of now", Toast.LENGTH_LONG)
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
            @SuppressLint("HandlerLeak")
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
    fun newDoc(message: String, id:String) {

        val serverURL: String = "${url}newdoc/user=${User.id}&id=$id"
        println(serverURL)
        println(id)
        println(message)
        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
                .addHeader("data", message)
                .url(serverURL)
                .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("fail")
            }

            override fun onResponse(call: Call, response: Response) {
                println("call back: " + response.message)
            }
        })


    }
    fun newReminder(ctx:Activity){
        var alert = AlertDialog.Builder(ctx)
        val inflate = ctx.layoutInflater.inflate(R.layout.layout, null)
        alert.setView(inflate)
        var create: AlertDialog? = null
        alert.setPositiveButton("ok") { dialog, id ->
           val title = create?.findViewById<TextView>(R.id.reminderName)?.text.toString()
            println(title)
            println(URL("$url/remindernn/name=$title&user=${User.id}").readText())
        }
        create = alert.create()
        create.show()

    }

}