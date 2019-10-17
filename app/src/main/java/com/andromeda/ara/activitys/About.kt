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
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.andromeda.ara.R
import com.andromeda.ara.R.id
import com.andromeda.ara.R.layout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

/**
 * About Class
 *
 */
class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_about)
        val toolbar: Toolbar = findViewById(id.toolbar)
        setSupportActionBar(toolbar)
        val fab: FloatingActionButton = findViewById(id.fab)
        fab.setOnClickListener { view: View? ->
            Snackbar.make(view!!, getString(R.string.replace_with_your_own_action), Snackbar.LENGTH_LONG)
                    .setAction(getString(R.string.action), null).show()
        }
        Objects.requireNonNull(supportActionBar)!!.setDisplayHomeAsUpEnabled(true)
    }
}