package com.mor.eye.view.base

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import com.mor.eye.R
import com.mor.eye.repository.remote.RetrofitException
import com.mor.eye.util.AppManager
import com.mor.eye.util.other.DataSourceConstant
import com.mor.eye.util.other.bindable
import com.mor.eye.util.other.showToast
import com.mor.eye.view.detail.activity.*
import com.mor.eye.view.gallery.GalleryActivity
import me.yokeyword.fragmentation.SupportActivity
import org.koin.android.ext.android.inject

abstract class BaseSupportActivity: SupportActivity() {
    protected var rootView: View? = null
    protected val appManager: AppManager by inject()

    val mToolbar: Toolbar by bindable(R.id.toolbar)
    lateinit var callbacks: Callbacks

    override fun onCreate(savedInstanceState: Bundle?) {
        appManager.addActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(getContentViewResId())
        initImmersion()
        if (showToolBar()) {
            setSupportActionBar(mToolbar);
            supportActionBar?.setDisplayShowTitleEnabled(false);
        }
        initCallback()
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        if (layoutResID == 0) {
            throw RuntimeException("layoutResID==-1 have u create your layout?")
        }

        if (showToolBar() && getToolBarResId() != -1) {
            //如果需要显示自定义toolbar,并且资源id存在的情况下，实例化baseView;
            rootView = LayoutInflater.from(this).inflate(if (toolbarCover())
                R.layout.ac_base_toolbar_cover
            else
                R.layout.ac_base, null, false)//根布局

            val mVs_toolbar = rootView?.findViewById(R.id.vs_toolbar) as ViewStub//toolbar容器
            val fl_container = rootView?.findViewById(R.id.fl_container) as FrameLayout//子布局容器
            mVs_toolbar.layoutResource = getToolBarResId()//toolbar资源id
            mVs_toolbar.inflate()//填充toolbar
            LayoutInflater.from(this).inflate(layoutResID, fl_container, true)//子布局
            setContentView(rootView)
        } else {
            //不显示通用toolbar
            super.setContentView(layoutResID)
        }
    }

    /**
     * 初始化沉浸式
     */
    open fun initImmersion() {}

    /**
     * 是否显示通用toolBar
     */
    open fun showToolBar(): Boolean {
        return false
    }

    /**
     * toolbar是否覆盖在内容区上方
     *
     * @return false 不覆盖  true 覆盖
     */
    open fun toolbarCover(): Boolean {
        return false
    }

    /**
     * 获取自定义toolbarview 资源id 默认为-1，showToolBar()方法必须返回true才有效
     */
    open fun getToolBarResId(): Int {
        return R.layout.layout_common_toolbar
    }

    /**
     * 获取contentView 资源id
     */
    abstract fun getContentViewResId(): Int

    private fun initCallback() {
        setCallback {
            categoryClick { id, iconUrl -> CategoriesDetailActivity.open(this@BaseSupportActivity, id.toString(), iconUrl) }
            authorClick { id, userType -> AuthorDetailActivity.open(this@BaseSupportActivity, id.toString(), userType) }
            videoClick { videoId, feedUrl, playUrl, blurUrl, videoTitle -> VideoDetailActivity.open(this@BaseSupportActivity, videoId, feedUrl, playUrl, blurUrl, videoTitle) }
            dynamicInfoClick { showToast("dynamicInfo") }
            bannerClick { url ->
                if (url.contains(DataSourceConstant.EYEPETIZER)) {
                    WebDetailActivity.open(this@BaseSupportActivity, url)
                } else {
                    val intent = Intent("android.intent.action.VIEW")
                    val uri = Uri.parse(url)
                    intent.data = uri
                    startActivity(intent)
                }
            }
            toggleClick { showToast("toggle") }
            tagClickListener { id -> TagsDetailActivity.open(this@BaseSupportActivity, id.toString()) }
            pictureClick { index, urls, photo -> GalleryActivity.open(this@BaseSupportActivity, index, urls, photo) }
            autoPlayVideoClick { _ -> showToast("autoPlayVideo") }
        }
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

    private fun setCallback(cb: Callbacks) {
        callbacks = cb
    }

    private fun setCallback(callbacks: CallbacksExtensions.() -> Unit) {
        val cb = CallbacksExtensions()
        cb.callbacks()
        setCallback(cb)
    }

    override fun onDestroy() {
        super.onDestroy()
        rootView = null
        appManager.finishActivity(this)
    }
}