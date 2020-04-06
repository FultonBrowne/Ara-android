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
import android.graphics.drawable.Drawable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.client.models.FeedModel
import java.io.InputStream
import java.net.URL



class Adapter(private val mFeedModels: ArrayList<FeedModel>, val act:Activity) : RecyclerView.Adapter<Adapter.FeedModelViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): FeedModelViewHolder {
        //Inflate the card view
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return FeedModelViewHolder(v)
    }


    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        //set values
        val rssFeedModel = mFeedModels[position]
        val desc = (holder.rssFeedView.findViewById<View>(R.id.item_number) as TextView)
        val card = (holder.rssFeedView.findViewById<View>(R.id.card) as CardView)
        if (rssFeedModel.color != null)
        card.setCardBackgroundColor(rssFeedModel.color!!)

        desc.text = rssFeedModel.description
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = LengthFilter(40)

        if (!rssFeedModel.longText) desc.filters = filterArray



        (holder.rssFeedView.findViewById<View>(R.id.content) as TextView).text = rssFeedModel.title
        if(rssFeedModel.image != "") {
            Thread {
                println("image")
                try {
                    val url = URL(rssFeedModel.image)
                    val `is` = URL(url.toString()).content as InputStream
                    val round = Drawable.createFromStream(`is`, "src")
                    act.runOnUiThread {
                    holder.rssFeedView.findViewById<ImageView>(R.id.item_image_view).setImageDrawable(round)}
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.start()


        }
        //animate
        setFadeAnimation(holder.itemView)
    }

    override fun getItemCount(): Int {
        //get the item count of the list
        return mFeedModels.size
    }

    //animation code
    private fun setFadeAnimation(view: View) {
        val anim = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 1000
        view.startAnimation(anim)
    }

    class FeedModelViewHolder(val rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)
}

