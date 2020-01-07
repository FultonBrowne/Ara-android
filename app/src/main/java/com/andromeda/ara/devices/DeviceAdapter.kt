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
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.constants.ServerUrl
import com.andromeda.ara.constants.User
import java.net.URL
import java.util.ArrayList

class DeviceAdapter(finalDevices:ArrayList<FinalDevice>, ctx: Context, docId:String): RecyclerView.Adapter<DeviceAdapter.MainVH>() {
    var mainVHThing= finalDevices
    val ctx1 = ctx
    val id = docId
    class MainVH(var rssFeedView: View) : RecyclerView.ViewHolder(rssFeedView)

    override fun onBindViewHolder(holder: MainVH, position: Int) {
        try {
            val mainBool = mainVHThing[position].value as Boolean
            val desc = (holder.rssFeedView.findViewById<View>(R.id.num1switch) as Switch)
            desc.text = mainVHThing[position].name
            desc.isChecked = mainBool
            desc.setTextColor(ctx1.resources.getColor(R.color.md_black_1000))
            desc.setOnClickListener {
                val url = URL(ServerUrl.url + "/devices/" + "id=" + id + "&user=" + User.id + "&run=" + mainVHThing[position].name + ":" + desc.isChecked )
                Thread(){
                url.readText()}.run()
            }
            desc.visibility = View.VISIBLE
            holder.rssFeedView
            return

        }
        catch (e : Exception){
            e.printStackTrace()
        }
            var mainInt = mainVHThing[position].value as Int?
            val num = holder.rssFeedView.findViewById<View>(R.id.num1String) as EditText
            if(mainInt == null) mainInt = 0
            num.setText(mainInt.toString())
            num.visibility = View.VISIBLE
            num.setTextColor(ctx1.resources.getColor(R.color.md_black_1000))
        num.visibility = View.VISIBLE
holder.rssFeedView
        return


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