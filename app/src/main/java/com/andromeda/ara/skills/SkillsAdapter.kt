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


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.util.SkillsModel
import java.util.ArrayList


class SkillsAdapter(private val list: List<SkillsModel>, act: Activity) : RecyclerView.Adapter<SkillsAdapter.FeedModelViewHolder>() {
    val activity = act
    val outList = ArrayList<TempSkillsStore>()
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): FeedModelViewHolder {
        //Inflate the card view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_skills, parent, false)

        return FeedModelViewHolder(v)
    }


    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        //set values
        val model = list[position]
        val mainNum = position
        var skills: SkillsModel = model
        var toOut: TempSkillsStore? = null
        val desc = (holder.rssFeedView.findViewById<View>(R.id.item_number) as TextView)
        desc.text = model.action
        val spinner = holder.rssFeedView.findViewById<View>(R.id.spinner_task) as Spinner
        val adapter1: ArrayAdapter<CharSequence?> = ArrayAdapter.createFromResource(this.activity.applicationContext, R.array.action_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter1;
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View,
                                        position: Int, id: Long) {
                val text = SkillsMap().map(position)
                skills = SkillsModel(text, skills.arg1, skills.arg2)
                toOut = TempSkillsStore(skills, mainNum)

            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        }
        //TODO work on this
        spinner.setSelection(1)
        //Creating the instance of ArrayAdapter containing list of fruit names
        //Creating the instance of ArrayAdapter containing list of fruit names
        val language = arrayOf("topic from command")
        val adapter: ArrayAdapter<String> = ArrayAdapter(this.activity.applicationContext, android.R.layout.select_dialog_item, language)

        val arg1Text = holder.rssFeedView.findViewById<View>(R.id.arg1) as AutoCompleteTextView
        val arg2Text = holder.rssFeedView.findViewById<View>(R.id.arg2) as AutoCompleteTextView
        arg1Text.setAdapter(adapter)
        arg2Text.setAdapter(adapter)
        arg1Text.setOnDismissListener {
            val text = arg1Text.text.toString()
            skills = SkillsModel(model.action, text, skills.arg2)
            toOut = TempSkillsStore(skills, mainNum)
        }


        arg2Text.setOnDismissListener { val text = arg1Text.text.toString()
            skills = SkillsModel(model.action, skills.arg1, text)
            toOut = TempSkillsStore(skills, mainNum)
        }



        setFadeAnimation(holder.itemView)

    }

    override fun getItemCount(): Int {
        //get the item count of the list
        return list.size
    }

    //animation code
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }

    class FeedModelViewHolder(val rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)
}


