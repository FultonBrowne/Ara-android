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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R


class Adapter(private val mRssFeedModels: List<RssFeedModel>) : RecyclerView.Adapter<Adapter.FeedModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): FeedModelViewHolder {
        //Inflate the card view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return FeedModelViewHolder(v)
    }


    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        //set values
        val rssFeedModel = mRssFeedModels[position]
        (holder.rssFeedView.findViewById<View>(R.id.item_number) as TextView).text = rssFeedModel.description
        (holder.rssFeedView.findViewById<View>(R.id.content) as TextView).text = rssFeedModel.title
        //animate
        setFadeAnimation(holder.itemView)
    }

    override fun getItemCount(): Int {
        //get the item count of the list
        return mRssFeedModels.size
    }

    //animation code
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }

    class FeedModelViewHolder(val rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)
}

