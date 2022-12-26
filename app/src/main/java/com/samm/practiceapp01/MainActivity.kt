package com.samm.practiceapp01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*
    Todo:
        - If no results are found, do not hide the toolbar
        - Results text labels are placed in an ugly way
        - Close the keyboard after search button is pressed
        - Search button in keyboard?
        - Clean up code -- commented out code, unused code.
        - Refresh Button
        - Material Design
        - Bottom Nav drawer -- when there's a reason for another page e.g Breaking News page
        - NavGraph
        - Database for Caching and to favorite news cards
        - Change project name
        - Github Repository!!
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