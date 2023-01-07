package com.samm.practiceapp01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
    Todo (Issues):
        - Not all page data will load - possibly due to the free version of the API
 */

/*
    Todo (Tasks):
        - Room database for caching
        - Sealed Class for state
        - Tests
        - Clean up
        - Bottom Nav drawer
        - NavGraph
        - Change project name
        - ViewBinding
        - Hilt
        - Create a Breaking News Fragment. - Optional
        - Sorting the list
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