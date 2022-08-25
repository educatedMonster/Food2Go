package com.example.kafiesta.screens.main.fragment.order.others.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.example.kafiesta.R
import com.example.kafiesta.databinding.DialogWebViewBinding
import com.trackerteer.taskmanagement.utilities.extensions.gone

class WebViewDialog(
    webUrl: String,
    private val listener: Listener
) : DialogFragment() {

    private lateinit var binding: DialogWebViewBinding
    private lateinit var mWebSettings: WebSettings
    private var mWebUrl = webUrl

    interface Listener {
        fun onCloseClicked()
        fun onOpenActivityUrl(url: String)
    }

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    @SuppressLint("DefaultLocale")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        //Initialize the AlertDialog.Builder
        val builderDialog = AlertDialog.Builder(context)
        //Initialize LayoutInflater
        val layoutInflater = LayoutInflater.from(context)
        //Initialize DataBindingUtil
        //**make sure your layout was inside the <layout> view

        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_web_view,
            null,
            false
        ) as DialogWebViewBinding

        //Use the binding.root to get the view on our binding
        val view = binding.root

        initConfig()

        builderDialog.setView(view)

        val alertDialog = builderDialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(true)
        alertDialog.setCanceledOnTouchOutside(true)

        return alertDialog
    }

    private fun initConfig() {
        binding.apply {
            btnClose.setOnClickListener {
                listener.onCloseClicked()
            }

            mWebSettings = webview.settings
            webview.webChromeClient = MyWebChromeClient(binding.activeProgress, mWebUrl, listener)
            webview.webViewClient = WebViewClient()
            webview.loadUrl(mWebUrl)
        }

        mWebSettings.javaScriptEnabled = true
        mWebSettings.loadWithOverviewMode = true
        mWebSettings.useWideViewPort = true
        mWebSettings.builtInZoomControls = true
        mWebSettings.displayZoomControls = false
        mWebSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
    }

    private class MyWebChromeClient(
        private val progressBar: ProgressBar,
        webUrl: String,
        listener: Listener
    ) : WebChromeClient() {
        private val mWebHost: String? = Uri.parse(webUrl).host
        private val mListener: Listener = listener

        override fun onProgressChanged(view: WebView, newProgress: Int) {
            val pageHostName = Uri.parse(view.url).host
            if (!pageHostName!!.matches(mWebHost!!.toRegex())) {
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                mListener.onOpenActivityUrl(view.url!!)
                return
            }
            progressBar.progress = newProgress
            if (newProgress == 100) {
                progressBar.gone()
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            super.onReceivedTitle(view, title)
        }
    }
}