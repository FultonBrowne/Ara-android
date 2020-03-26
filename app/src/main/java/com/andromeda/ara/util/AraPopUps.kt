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
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputType
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.ServerUrl.url
import com.andromeda.ara.constants.User
import com.andromeda.ara.constants.User.id
import com.andromeda.ara.iot.SetUp
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


class AraPopUps {
    val c = Calendar.getInstance()
    private var year = c[Calendar.YEAR]
    private var month = c[Calendar.MONTH]
    private var day = c[Calendar.DAY_OF_MONTH]
    private val hour = c[Calendar.HOUR_OF_DAY]
    private val minute = c[Calendar.MINUTE]
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
            URL("${url}updateuserdata/user=${User.id}id=$id&prop=name&newval=$text")
        }

            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }
    private fun newDevice(ctx: Context){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Title")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            val i = (Math.random() * (30000 + 1)).toInt()
            val url1 = URL("${ServerUrl.url}newdevice/user=${id}&id=$i")
            GetUrlAra().getIt(url1)

        }
        builder.show()

    }
    fun newHaDevice(ctx: Activity){
        var create: AlertDialog? = null
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setView(R.layout.new_iot_service)
        builder.setPositiveButton("go"){ dialogInterface: DialogInterface, i: Int ->
            val url = create!!.findViewById<EditText>(R.id.new_iot_url).text.toString()
            val key = create!!.findViewById<EditText>(R.id.new_iot_key).text.toString()
            SetUp().setUp(key, url, ctx)

        }
        create = builder.create()
        create.show()

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
            newDevice(ctx)
        }
        builder.show()

    }
    private var resultValue = "null"
    @SuppressLint("HandlerLeak")
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
        var time:Long? = 0
        var alert = AlertDialog.Builder(ctx)
        val inflate = ctx.layoutInflater.inflate(R.layout.layout, null)
        alert.setView(inflate)
        var create: AlertDialog? = null
        alert.setPositiveButton("ok") { dialog, id ->
           val title = create?.findViewById<TextView>(
                   R.id.reminderName)?.text.toString()
            println(title)
            val info = create?.findViewById<TextView>(R.id.reminderTitle)?.text.toString()
            println(URL("$url/remindernn/name=$title&user=${User.id}&time=$time&info=$info".replace(" ", "%20")).readText())
        }
        create = alert.create()
        create.show()
        var button = create.findViewById<Button>(R.id.popupDialogButton)
        button.setOnClickListener {
            time = getTime(ctx)

        }

    }

    private fun getTime(ctx: Activity): Long {
        var mDateTime1 = ""
        var time1 = 0L
        DatePickerDialog(
                ctx, OnDateSetListener { view, year, month, dayOfMonth ->
            mDateTime1 = "$year-$month-$dayOfMonth"
            this.year = year
            this.month = month
            this.day = dayOfMonth
            Handler().postDelayed({
                TimePickerDialog(ctx, OnTimeSetListener { view, hourOfDay, minute ->
                    mDateTime1 += " $hourOfDay:$minute"
                }, hour, minute, true).show()
                time1 = Date.UTC(this.year, this.month, this.day, this.hour, this.minute, 0)
            }, 500)
        }, year, month, day).show()
        return time1
    }

    fun editReminder(ctx:Activity, id:String){
        val readText = URL(ServerUrl.getReminder(id)).readText()
        println(readText)
        val reminder = JsonParse().reminder(readText)[0]
        var time:Long? = reminder.time
        var alert = AlertDialog.Builder(ctx)
        val inflate = ctx.layoutInflater.inflate(R.layout.layout, null)
        alert.setView(inflate)
        var create: AlertDialog? = null
        alert.setPositiveButton("ok") { dialog, id ->
            val title = create?.findViewById<TextView>(
                    R.id.reminderName)?.text.toString()
            println(title)
            val info = create?.findViewById<TextView>(R.id.reminderTitle)?.text.toString()
            println(URL("$url/reminderu/name=$title&user=${User.id}&time=$time&info=$info&id=$id".replace(" ", "%20")).readText())
        }
        create = alert.create()
        create.show()
        create?.findViewById<TextView>(
                R.id.reminderName)?.text = reminder.header
        create?.findViewById<TextView>(R.id.reminderTitle)?.text = reminder.body
        var button = create.findViewById<Button>(R.id.popupDialogButton)
        button.setOnClickListener {
            time = getTime(ctx)

        }
    }
    fun delete(ctx: Activity,  id: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
        builder.setTitle("Are you sure you want to delete this reminder")
        val input = EditText(ctx)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Yes") { _, _ ->
            println(URL("$url/reminderd/&user=${User.id}&id=$id".replace(" ", "%20")).readText())
        }
        builder.setNeutralButton("help"){ dialogInterface: DialogInterface, i: Int ->
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FultonBrowne/Ara-android/wiki/How-to-set-up-Home-assistant-with-Ara"))
            ctx.startActivity(browserIntent)
        }
        builder.show()

    }





}

