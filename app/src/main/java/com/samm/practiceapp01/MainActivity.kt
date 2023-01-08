package com.samm.practiceapp01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
    Todo (Issues):
        - Don't use getDataByPage() in next and prev buttons click listeners; clears cache
        - Developer accounts are limited to 100 results
 */

/*
    Todo (Tasks):
        - Sealed Class for state
        - Clean up
        - NavGraph
        - Change project name
        - ViewBinding
        - Hilt
        - Create a Breaking News Fragment. - Optional
        - Tests
        - Play Store
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "News Articles"
    }
}