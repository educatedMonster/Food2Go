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
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.DialogWebViewBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.screens.main.fragment.order.OrderViewModel
import com.example.kafiesta.utilities.helpers.OrderRecyclerClick
import com.trackerteer.taskmanagement.utilities.extensions.gone

class DialogWebView(
    private val model: OrderBaseDomain,
    private val onClickCallBack: OrderRecyclerClick,
    private val listener: Listener
) : DialogFragment() {

    private lateinit var binding: DialogWebViewBinding
    private lateinit var mWebSettings: WebSettings
    private var mWebUrl = model.order.proofURL!!

    interface Listener {
        fun onCloseClicked()
    }

    override fun getTheme(): Int = R.style.NoMarginsDialog

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
    }

    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(OrderViewModel::class.java)
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
        binding.lifecycleOwner = this
        binding.model = model
        binding.orderViewModel = orderViewModel
        binding.onClickCallBack = onClickCallBack
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
            webview.webChromeClient = MyWebChromeClient(binding.activeProgress)
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
        private val progressBar: ProgressBar
    ) : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            Uri.parse(view.url).host
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