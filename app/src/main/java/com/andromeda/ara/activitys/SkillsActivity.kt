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

package com.andromeda.ara.activitys

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.skills.Parse
import com.andromeda.ara.skills.SkillsAdapter
import com.andromeda.ara.skills.TempSkillsStore
import com.andromeda.ara.skills.UserSkills
import com.andromeda.ara.util.FeedDateParseModel
import com.andromeda.ara.util.SkillsModel
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_skills.*
import java.lang.NullPointerException
import java.util.*

class SkillsActivity : AppCompatActivity() {
    var id = 0
    private var adapter:SkillsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_skills)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        val recView = findViewById<View>(R.id.listSkills) as RecyclerView
        recView.layoutManager = LinearLayoutManager(this)
        id = intent.getIntExtra("linktext", 0)
        val db = UserSkills(this)
        db.open()
        println("test")
        println(db.actFromId(id))
        val toAdapter = Parse().parse(db.actFromId(id))
        adapter = toAdapter?.toList()?.let { SkillsAdapter(it, this) }
        recView.adapter = adapter
        db.close()
    }

    fun save(view: View?) {
        val db = UserSkills(this)
        if (id == 0) throw NullPointerException("CAN NOT SAVE ID NULL")

        db.open()
        val list = adapter?.outList
        val sortedList = sortOrder(list)
        val toYAML = ArrayList<SkillsModel>()
        if (sortedList != null) {
            for (i in sortedList){
                toYAML.add(i.mainData)
            }
        }
        val mapper = ObjectMapper(YAMLFactory())

        db.insert(db.preFromId(id), db.nameFromId(id), mapper.writeValueAsString(toYAML), id)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        save(null)
        moveTaskToBack(false)
    }

    private fun sortOrder(tosort: ArrayList<TempSkillsStore>?): ArrayList<TempSkillsStore>? { //sort by order
        tosort?.sortBy { obj: TempSkillsStore -> obj.order }
        // return sorted value
        tosort?.reverse()
        return tosort
    }
    fun addItem(m:MenuItem){

    }

}
