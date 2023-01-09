package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
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

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val url = arguments?.getString(ARG_URL)

        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        val webView = view.findViewById<WebView>(R.id.web_view)
        webView.webViewClient = MyWebViewClient()

        val viewSettings = webView.settings
        viewSettings.javaScriptEnabled = true
        viewSettings.safeBrowsingEnabled = true

        if (url != null) {
            webView.loadUrl(url)
        }
        return view
    }
}

// Not sure if this is needed - investigating further
class MyWebViewClient : WebViewClient() {
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        // Handle the URL loading in the web view
        view.loadUrl(url)
        return true
    }
}
