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

package com.andromeda.ara.util



import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R


class SkillsAdapter(private val list: List<SkillsModel>, act:Activity) : RecyclerView.Adapter<SkillsAdapter.FeedModelViewHolder>() {
    val activity = act
    override fun onCreateViewHolder(parent: ViewGroup, type: Int): SkillsAdapter.FeedModelViewHolder {
        //Inflate the card view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_skills, parent, false)

        return FeedModelViewHolder(v)
    }


    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        //set values
        val model = list[position]
        val desc = (holder.rssFeedView.findViewById<View>(R.id.item_number) as TextView)
        desc.text = model.action
        val spinner = holder.rssFeedView.findViewById<View>(R.id.spinner_task) as Spinner
        var adapter1: ArrayAdapter<CharSequence?> = ArrayAdapter.createFromResource(this.activity.applicationContext, R.array.action_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(arg0: AdapterView<*>?, arg1: View,
                                        position: Int, id: Long) {
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {}
        })



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

