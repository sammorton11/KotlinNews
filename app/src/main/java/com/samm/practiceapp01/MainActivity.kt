package com.samm.practiceapp01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
    Todo:
        - Search button from the keyboard
        - Clean up
        - Bottom Nav drawer -- when there's a reason for another page e.g Breaking News page
        - NavGraph
        - Change project name
        - ViewBinding
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "Kotlin News"
    }
}