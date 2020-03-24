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

package com.andromeda.ara.iot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R

class IotAdapter(data:RequestModel):RecyclerView.Adapter<IotAdapter.FeedModelViewHolder>() {
    val mainData = data
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):FeedModelViewHolder {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.iot_layout, parent, false)
        return FeedModelViewHolder(v)
    }


    override fun getItemCount(): Int {
        return mainData.attributes.size
    }


    class FeedModelViewHolder(rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)
    override fun onBindViewHolder(holder: FeedModelViewHolder, position: Int) {
        val itemView = holder.itemView
        if(mainData.attributes[position] is Boolean){
            val iotSwitch = itemView.findViewById<Switch>(R.id.iot_switch)
            iotSwitch.visibility = View.VISIBLE
        }
        else {
            val iotText = itemView.findViewById<EditText>(R.id.iot_edit)
            iotText.visibility = View.VISIBLE
        }
    }

}