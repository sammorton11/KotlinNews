package com.samm.practiceapp01.core

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import java.text.SimpleDateFormat
import java.util.*

class ViewUtility {
    fun hideKeyboard(activity: FragmentActivity?) {
        val view = activity?.currentFocus
        if (view != null) {
            val inputManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    fun formatDate(input: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val outputFormat = SimpleDateFormat("MM-dd-yyyy - hh:mm a", Locale.US)
        val date = inputFormat.parse(input)

        return date?.let { outputFormat.format(it) }
    }
}
