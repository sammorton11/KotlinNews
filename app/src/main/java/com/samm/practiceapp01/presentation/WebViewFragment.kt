package com.samm.practiceapp01.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.samm.practiceapp01.R

class WebViewFragment : Fragment() {

    companion object {
        const val ARG_URL = "arg_url"

        fun newInstance(url: String): Fragment {
            val args = Bundle()
            args.putString(ARG_URL, url)
            val fragment = WebViewFragment()
            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        val webView = view.findViewById<WebView>(R.id.web_view)
        val url = arguments?.getString(ARG_URL)

        if (url != null) {
            webView.loadUrl(url)
        }
        return view
    }
}
