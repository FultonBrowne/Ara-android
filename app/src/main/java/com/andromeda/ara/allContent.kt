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

@file:Suppress("ClassName")

package com.andromeda.ara


import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.io.IOException
import java.io.InputStream
import java.net.URL


class allContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_content)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name: String? = intent.getStringExtra("NAME")
        val desc: String? = intent.getStringExtra("DESC")
        val pic: String? = intent.getStringExtra("PIC")
        var `is`: InputStream? = null
        try {
            `is` = URL(pic).content as InputStream
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val draw = Drawable.createFromStream(`is`, "src name")
        val collapsingToolbar = findViewById<View>(R.id.image) as ImageView
        collapsingToolbar.minimumHeight = draw.minimumHeight

        collapsingToolbar.background = draw


        val txtv = findViewById<View>(R.id.maintxt1) as TextView
        txtv.text = desc
        supportActionBar?.title = name


    }
}
