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
import android.widget.Switch
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.andromeda.ara.R
import kotlinx.android.synthetic.main.activity_prefs.*

class PrefsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prefs)
        setSupportActionBar(toolbar)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val switch1 = findViewById<Switch>(R.id.switch1)
        val switch2 = findViewById<Switch>(R.id.switch2)
        val switch3 = findViewById<Switch>(R.id.switch3)
        val switch4 = findViewById<Switch>(R.id.switch4)
        val switch5 = findViewById<Switch>(R.id.switch5)
        switch1.isChecked = prefs.getBoolean("getData", true)
        switch2.isChecked = prefs.getBoolean("araAccount", true)
        switch3.isChecked = prefs.getBoolean("EAS", false)
        switch4.isChecked = prefs.getBoolean("heyAra", true)
        switch5.isChecked = prefs.getBoolean("notify", true)



    }
}
