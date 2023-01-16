package com.samm.practiceapp01

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/*
    Todo (Tasks)
        - Create a Breaking News Fragment. - Optional
        - Tests need to be changes since adding the database for caching purposes
        - Play Store
 */
/*
    Todo (Tests):
        - Api to Database integration
        - Database to Repository integration
        - Repo to ViewModel integration
        - ViewModel to UI integration
        - Unit tests for business logic stuff
        - UI Tests:
            - Cards are loaded and are present
            - Search view is present
            - Title is present in actionbar
            - Back to top button is present when scrolled and hidden when at the top
            - Colors of each component
            - Cards are clickable
            - Cards open Web View fragment when clicked
            - All data is loaded into Recycler View
            - Back button brings user to Main Screen
            - When error is present - Error message is displayed
            - Scroll bar is present
            - User can type in search field
            - Data is retrieved when searched
            - Progress bar is present when loading and hidden after
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "News Articles"
    }
}