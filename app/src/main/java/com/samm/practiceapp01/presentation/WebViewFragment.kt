package com.samm.practiceapp01.presentation

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.samm.practiceapp01.R

class WebViewFragment : Fragment() {

    private var url : String = "url"
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            url = it.getString("url")!!
        }

        val view = inflater.inflate(R.layout.fragment_web_view, container, false)
        webView = view.findViewById<WebView>(R.id.web_view)
        webView.webViewClient = MyWebViewClient()

        val viewSettings = webView.settings
        viewSettings.javaScriptEnabled = true
        viewSettings.safeBrowsingEnabled = true
        webView.loadUrl(url)

        return view
    }
}

// Not sure if this is needed - investigating further
class MyWebViewClient : WebViewClient() {
    @Deprecated("Deprecated in Java")
    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        view.loadUrl(url)
        return true
    }
}
