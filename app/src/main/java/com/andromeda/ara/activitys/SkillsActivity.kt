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

package com.andromeda.ara.activitys

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.SkillsAdapter
import com.andromeda.ara.skills.TempSkillsStore
import com.andromeda.ara.util.AraPopUps
import com.andromeda.ara.util.SkillsDBModel
import com.andromeda.ara.util.SkillsModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.microsoft.appcenter.data.Data
import com.microsoft.appcenter.data.DefaultPartitions
import kotlinx.android.synthetic.main.activity_skills.*
import java.util.*


class SkillsActivity : AppCompatActivity() {
    var id: String? = ""
    private var adapter: SkillsAdapter? = null
    var name = ""
    private var runOn = ""
    private var recView: RecyclerView? = null
    private var allData:SkillsDBModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills)
        setSupportActionBar(toolbar)
        recView = findViewById<View>(R.id.listSkills) as RecyclerView
        recView!!.layoutManager = LinearLayoutManager(this)
        id = intent.getStringExtra("linktext")
        reload()
    }

    fun save(view: View?) {
        if (id == "") throw NullPointerException("CAN NOT SAVE ID NULL")
        val list = adapter?.outList
        val sortedList = sortOrder(list)
        val toYAML = ArrayList<SkillsModel>()
        if (sortedList != null) for (i in sortedList) toYAML.add(i.mainData)
        else throw NullPointerException()
        val mapper = ObjectMapper(YAMLFactory())
        val yml = mapper.writeValueAsString(toYAML)
        println(yml)
        Data.replace(id, SkillsDBModel(SkillsModel(yml, runOn, ""), name, this!!.id!!), SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS)
        onBackPressed()
    }


    private fun sortOrder(tosort: ArrayList<TempSkillsStore>?): ArrayList<TempSkillsStore>? { //sort by order
        tosort?.sortBy { obj: TempSkillsStore -> obj.order }
        // return sorted value
        tosort?.reverse()
        return tosort
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_skills, menu);
        return true
    }

    fun addItem(m: MenuItem) {
        if (id == "") throw NullPointerException("CAN NOT SAVE ID NULL")
        val list = adapter?.outList
        val sortedList = sortOrder(list)
        val toYAML = ArrayList<SkillsModel>()
        if (sortedList != null) {
            for (i in sortedList) {
                toYAML.add(i.mainData)
            }
        } else throw NullPointerException()
        toYAML.add(SkillsModel("CALL", "", ""))
        val mapper = ObjectMapper(YAMLFactory())

        val yml = mapper.writeValueAsString(toYAML)
        println(yml)
        Data.replace(id, SkillsDBModel(SkillsModel(yml, runOn, ""), name, this!!.id!!), SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS)
       reload()
    }

    fun voicePhrase(item: MenuItem) {}
    fun rename(item: MenuItem) {
        id?.let { allData?.let { it1 -> AraPopUps().renameSkill(this, it, it1) } }
        reload()
    }
    private fun reload(){
        Data.read(id, SkillsDBModel::class.java, DefaultPartitions.USER_DOCUMENTS).thenAccept { userDocumentWrapper ->
            if (userDocumentWrapper.error == null) {
                runOnUiThread {
                    allData = userDocumentWrapper.deserializedValue
                    val actionToRun = userDocumentWrapper.deserializedValue.action.action
                    val toAdapter = Parse().parse(actionToRun);
                    name = userDocumentWrapper.deserializedValue.name
                    runOn = userDocumentWrapper.deserializedValue.action.arg1
                    adapter = toAdapter?.toList()?.let { SkillsAdapter(it, this) }
                    recView?.adapter = adapter
                }

            }
        }
    }

}
