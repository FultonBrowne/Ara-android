package com.andromeda.ara

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class allContent : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_content)
        // actionBar.setDisplayHomeAsUpEnabled(true)
        val extras = intent.extras
        val name = extras?.get("NAME")
        val txtv = findViewById<View>(R.id.maintxt1) as TextView


    }
}
