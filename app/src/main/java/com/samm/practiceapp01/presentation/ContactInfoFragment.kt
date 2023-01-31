package com.samm.practiceapp01.presentation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.samm.practiceapp01.R

class ContactInfoFragment : Fragment() {

    private lateinit var clickableEmail: TextView
    private lateinit var contactButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_contact_info, container, false )
        clickableEmail = view.findViewById(R.id.email)
        contactButton = view.findViewById(R.id.contactButton)
        contactButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "plain/text"
                putExtra(Intent.EXTRA_EMAIL, arrayOf(clickableEmail.text))
                putExtra(Intent.EXTRA_SUBJECT, "subject")
                putExtra(Intent.EXTRA_TEXT, "mail body")
            }
            startActivity(Intent.createChooser(intent, "") )
        }

        return view
    }
}