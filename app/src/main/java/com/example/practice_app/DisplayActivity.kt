package com.example.practice_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display)

        val bundle:Bundle? = intent.extras
        val message: String? = bundle?.getString(MainActivity.EXTRA_KEY)
//        val message:String? = intent.getStringExtra(MainActivity.EXTRA_KEY)
        val textview:TextView = findViewById(R.id.textView)
        textview.text = message
    }
}