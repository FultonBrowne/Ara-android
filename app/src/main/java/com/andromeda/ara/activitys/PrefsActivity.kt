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

package com.andromeda.ara.activitys

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
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
        val spinner = findViewById<Spinner>(R.id.timespinner)
        val switch1 = findViewById<Switch>(R.id.switch1)
        val switch2 = findViewById<Switch>(R.id.switch2)
        val switch3 = findViewById<Switch>(R.id.switch3)
        val switch4 = findViewById<Switch>(R.id.switch4)
        val switch5 = findViewById<Switch>(R.id.switch5)
        val switch6 = findViewById<Switch>(R.id.useOtherServer)
        val text = findViewById<EditText>(R.id.otherServer)
        switch1.isChecked = prefs.getBoolean("getData", true)
        switch2.isChecked = prefs.getBoolean("araAccount", true)
        switch3.isChecked = prefs.getBoolean("EAS", false)
        switch4.isChecked = prefs.getBoolean("heyAra", true)
        switch5.isChecked = prefs.getBoolean("notify", true)
        switch6.isChecked = prefs.getBoolean("useOther", false)
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item)
        dataAdapter.add("30 minutes")
        dataAdapter.add("60 minutes")
        dataAdapter.add("15 minutes")
        spinner.adapter = dataAdapter
        switch1.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("getData", isChecked).apply()
        }
        switch2.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("araAccount", isChecked).apply()
        }
        switch3.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("EAS", isChecked).apply()
        }
        switch4.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("heyAra", isChecked).apply()
        }
        switch5.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notify", isChecked).apply()
        }
        switch6.setOnCheckedChangeListener {_, isChecked ->
            prefs.edit().putBoolean("useOther", isChecked).apply()
        }
        text.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                prefs.edit().putString("serverText", s.toString()).apply()
            }

        })


    }

    fun account(view: View) {

        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://AraLogIn.b2clogin.com/AraLogIn.onmicrosoft.com/oauth2/v2.0/authorize?p=B2C_1_changeInfo&client_id=e4e16983-2565-496c-aa70-8fe0f1bf0907&nonce=defaultNonce&redirect_uri=https%3A%2F%2Fjwt.ms&scope=openid&response_type=id_token&prompt=login"));
        startActivity(browserIntent)
    }

    fun passwordReset(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://AraLogIn.b2clogin.com/AraLogIn.onmicrosoft.com/oauth2/v2.0/authorize?p=B2C_1_passwordReset&client_id=c6063f12-fa37-47bc-aa5d-604e60d197c2&nonce=defaultNonce&redirect_uri=https%3A%2F%2Faralogin.b2clogin.com%2Faralogin.onmicrosoft.com%2Foauth2%2Fauthresp&scope=openid&response_type=code&prompt=login"));
        startActivity(browserIntent)
    }

    fun bug(view: View) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/FultonBrowne/Ara-android/issues"));
        startActivity(browserIntent)
    }
}
