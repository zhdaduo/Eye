package com.mor.eye.view.detail.activity

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.mor.eye.R
import com.mor.eye.util.StringUtils
import com.mor.eye.util.other.WebArgumentConstant.Companion.WEB_URL
import com.mor.eye.util.other.show
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseActivity
import java.io.UnsupportedEncodingException

class WebDetailActivity : BaseActivity() {
    private val url by unsafeLazy { intent.getStringExtra(WEB_URL) }
    private lateinit var mWebView: WebView
    override fun getLayout(): Int = R.layout.activity_web_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 视频为了避免闪屏和透明问题
        window.setFormat(PixelFormat.TRANSLUCENT)
    }

    override fun init() {
        val webUrl = getURLDecoderString(url)
        initWebToolbar(webUrl)
        initWebView()
        mWebView.loadUrl(webUrl.split("url=")[1])
    }

    private fun initWebToolbar(webUrl: String) {
        initToolbar()

        tvBoldTitle.apply {
            show()
            text = StringUtils.formatUrl(webUrl)
            isSelected = true
        }
    }

    private fun initWebView() {
        val linearLayout = findViewById<LinearLayout>(R.id.linear_container)

        mWebView = WebView(applicationContext)
        mWebView = createWebView(mWebView)
        mWebView.webViewClient = object : WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        val lp = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        mWebView.layoutParams = lp
        linearLayout.addView(mWebView)
    }

    override fun onPause() {
        super.onPause()
        mWebView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mWebView.onResume()
    }

    public override fun onDestroy() {
        android.os.Process.killProcess(android.os.Process.myPid())
        super.onDestroy()
    }

    private fun getURLDecoderString(str: String?): String {
        var result = ""
        if (null == str) {
            return ""
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }

        return result
    }

    private fun formatUrl(url: String): String {
        return when {
            url.contains("date=") -> url.split("date=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            url.contains("start=") -> url.split("start=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            url.contains("title=") -> url.split("title=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            else -> url.split("page=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun createWebView(webView: WebView): WebView {
        WebView.setWebContentsDebuggingEnabled(true)
        // 不能横向滚动
        webView.isHorizontalScrollBarEnabled = false
        // 不能纵向滚动
        webView.isVerticalScrollBarEnabled = false
        // 允许截图
        webView.isDrawingCacheEnabled = true
        // 屏蔽长按事件
        webView.setOnLongClickListener { true }
        // 初始化WebSettings
        val settings = webView.settings
        settings.javaScriptEnabled = true
        val ua = settings.userAgentString
        settings.userAgentString = ua + "Latte"
        // 隐藏缩放控件
        settings.builtInZoomControls = false
        settings.displayZoomControls = false
        // 禁止缩放
        settings.setSupportZoom(false)
        // 文件权限
        settings.allowFileAccess = true
        settings.allowFileAccessFromFileURLs = true
        settings.allowUniversalAccessFromFileURLs = true
        settings.allowContentAccess = true
        // 缓存相关
        settings.setAppCacheEnabled(true)
        settings.domStorageEnabled = true
        settings.databaseEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT

        return webView
    }

    companion object {
        const val ENCODE = "UTF-8"

        fun open(context: Context, url: String) = context.startKActivity(WebDetailActivity::class) {
            putExtra(WEB_URL, url)
        }
    }
}
