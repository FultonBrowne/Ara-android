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

import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R

class SkillsAdapter(private val list: List<SkillsModel>) : RecyclerView.Adapter<SkillsAdapter.FeedModelViewHolder>() {
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
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(40)

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


