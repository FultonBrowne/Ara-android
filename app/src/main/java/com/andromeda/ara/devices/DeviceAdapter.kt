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

package com.andromeda.ara.devices

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import java.util.ArrayList

class DeviceAdapter(finalDevices:ArrayList<FinalDevice>, ctx: Context): RecyclerView.Adapter<DeviceAdapter.MainVH>() {
    var mainVHThing= finalDevices
    val ctx1 = ctx
    class MainVH(var rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        try {
            val mainBool = mainVHThing[position].value as Boolean
            val desc = (holder.rssFeedView.findViewById<View>(R.id.num1switch) as Switch)
            desc.text = mainVHThing[position].name
            desc.isChecked = mainBool
            holder.rssFeedView

        }
        catch (e : Exception){
            e.printStackTrace()
        }
        try {
            val mainInt = mainVHThing[position].value as Int

        }
        catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun getItemCount(): Int {
        return mainVHThing.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainVH {
        val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout3, parent, false)
        return MainVH(v)
    }

}