package swu.xl.trafficsystem.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_webview.*
import swu.xl.trafficsystem.R
import swu.xl.trafficsystem.base.BaseActivity

class WebViewActivity : BaseActivity() {
    companion object {
        private const val URL = "SecondHouseURL"

        fun loadURL(context: Context, url: String) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra(URL, url)
            context.startActivity(intent)
        }
    }

    override fun getLayoutId() = R.layout.activity_webview

    @SuppressLint("SetJavaScriptEnabled")
    override fun initData() {
        val url = intent.getStringExtra(URL)
        web_view.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                return false
            }
        }
        web_view.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setAppCacheEnabled(true)
            loadWithOverviewMode = true
            setSupportZoom(true)
            useWideViewPort = true
            displayZoomControls = true
            layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
        }
        web_view.loadUrl(url)
    }

    override fun onBackPressed() {
        if (web_view.canGoBack()) {
            web_view.goBack()
        } else {
            super.onBackPressed()
        }
    }
}