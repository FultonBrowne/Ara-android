@file:Suppress("ClassName")

package com.andromeda.ara


import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class allContent : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_content)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val name: String? = intent.getStringExtra("NAME")
        val desc: String? = intent.getStringExtra("DESC")
        val txtv = findViewById<View>(R.id.maintxt1) as TextView
        txtv.text = desc
        supportActionBar?.title = name


    }
}
