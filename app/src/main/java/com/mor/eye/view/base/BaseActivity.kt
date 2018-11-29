package com.mor.eye.view.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.widget.TextView
import com.mor.eye.R
import com.mor.eye.repository.remote.RetrofitException
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.DataSourceConstant.Companion.EYEPETIZER
import com.mor.eye.util.other.showToast
import com.mor.eye.view.detail.activity.*
import com.mor.eye.view.gallery.GalleryActivity

abstract class BaseActivity : AppCompatActivity() {
    var toolbar: Toolbar? = null
    lateinit var tvBoldTitle: TextView
    lateinit var callbacks: Callbacks
    var isBack = true
    private var mExitTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initCallback()
        init()
    }

    fun initToolbar() {
        tvBoldTitle = findViewById<TextView>(R.id.tv_bold_title)
        toolbar = findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
            supportActionBar?.title = ""
            setOnMenuItemClickListener(onMenuItemClick)
        }
        if (isBack) {
            toolbar?.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_black)
            toolbar?.setNavigationOnClickListener { onBackPressed() }
        }
    }

    var onMenuItemClick: Toolbar.OnMenuItemClickListener = Toolbar.OnMenuItemClickListener { menuItem ->
        onMenuItemClick(menuItem.itemId)
        true
    }

    fun onMenuItemClick(itemId: Int) {}

    fun onExitActivity(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK && event.action == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                showToast(R.string.exit_program_hint)
                mExitTime = System.currentTimeMillis()
            } else {
                android.os.Process.killProcess(android.os.Process.myPid())
                System.exit(0)
            }
            true
        } else {
            super.onKeyDown(keyCode, event)
        }
    }

    private fun initCallback() {
        setCallback {
            categoryClick { id, iconUrl -> CategoriesDetailActivity.open(this@BaseActivity, id.toString(), iconUrl) }
            authorClick { id, userType -> AuthorDetailActivity.open(this@BaseActivity, id.toString(), userType) }
            videoClick { videoData -> VideoDetailActivity.open(this@BaseActivity, videoData) }
            dynamicInfoClick { showToast("dynamicInfo") }
            bannerClick { url ->
                if (url.contains(EYEPETIZER)) {
                    WebDetailActivity.open(this@BaseActivity, url)
                } else {
                    val intent = Intent("android.intent.action.VIEW")
                    val uri = Uri.parse(url)
                    intent.data = uri
                    startActivity(intent)
                }
            }
            toggleClick { showToast("toggle") }
            tagClickListener { id -> TagsDetailActivity.open(this@BaseActivity, id.toString()) }
            pictureClick { index, urls, photo -> GalleryActivity.open(this@BaseActivity, index, urls, photo) }
            autoPlayVideoClick { _ -> showToast("autoPlayVideo") }
        }
    }

    abstract fun init()

    abstract fun getLayout(): Int

    private fun setCallback(cb: Callbacks) {
        callbacks = cb
    }

    private fun setCallback(callbacks: CallbacksExtensions.() -> Unit) {
        val cb = CallbacksExtensions()
        cb.callbacks()
        setCallback(cb)
    }

    protected fun showErrorToast(exception: Throwable) {
        if (exception is RetrofitException) {
            when (exception.getKind()) {

                RetrofitException.Kind.HTTP ->
                    showToast(R.string.default_http_error_message)

                RetrofitException.Kind.NETWORK ->
                    showToast(R.string.default_network_error_message)

                RetrofitException.Kind.UNEXPECTED ->
                    showToast(R.string.default_unexpected_error_message)
            }
        } else {
            showToast(R.string.default_http_error_message)
        }
    }
}