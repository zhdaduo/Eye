package com.mor.eye.view.detail.activity

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.gyf.barlibrary.BarHide
import com.gyf.barlibrary.ImmersionBar
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.glide.loadLocalImage
import com.mor.eye.util.other.bindable
import com.mor.eye.util.other.remove
import com.mor.eye.util.other.show
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.BaseSupportActivity
import com.ogaclejapan.smarttablayout.SmartTabLayout
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems

abstract class DetailBaseActivity : BaseSupportActivity() {

    private val appBarLayout: AppBarLayout by bindable(R.id.app_bar)
    private val viewPager: ViewPager by bindable(R.id.view_pager)
    private val smartTabLayout: SmartTabLayout by bindable(R.id.tab_view_pager)
    private val btnFocus: Button by bindable(R.id.btn_focus)
    private val toolbar: Toolbar by bindable(R.id.toolbar)
    private val tvToolbarTitle: TextView by bindable(R.id.tv_bold_title)
    private val ivCoverBg: ImageView by bindable(R.id.iv_cover_bg)
    private val tvName: TextView by bindable(R.id.tv_name)
    private val tvDescription: TextView by bindable(R.id.tv_description)

    @AppBarState
    private var mState: Int? = null
    lateinit var pageAdapter: FragmentPagerItemAdapter
    private val mImmersionBar by unsafeLazy { ImmersionBar.with(this) }

    override fun getContentViewResId(): Int = R.layout.activity_base_detail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupToolbar()
        initTab()
        initListener()
    }

    override fun initImmersion() {
        mImmersionBar
                .transparentStatusBar()
                .init()
    }

    private fun initListener() {
        appBarLayout.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {

                if (mState != EXPANDED) {
                    mState = EXPANDED // 修改状态标记为展开
                    tvToolbarTitle.remove()
                    toolbar.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange * 0.8) {
                if (mState != COLLAPSED) {
                    mState = COLLAPSED // 修改状态标记为折叠
                    tvToolbarTitle.show()
                    toolbar.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_black)
                }
            } else {
                if (mState != MIDDLE) {
                    if (mState == COLLAPSED) {
                        tvToolbarTitle.remove()
                        toolbar.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
                    }
                    mState = MIDDLE // 修改状态标记为中间
                }
            }
        }
    }

    private fun initTab() {
        pageAdapter = FragmentPagerItemAdapter(this@DetailBaseActivity.supportFragmentManager, getPages())
        viewPager.adapter = pageAdapter
        smartTabLayout.setViewPager(viewPager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = null
        toolbar.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    fun initUi(isFollowed: Boolean, title: String, imgUrl: String, description: String) {
        if (isFollowed) btnFocus.remove() else btnFocus.show()
        tvToolbarTitle.text = title
        if (imgUrl.isNullOrBlank()) {
            ivCoverBg.loadLocalImage(this, R.mipmap.cover_default)
        } else {
            ivCoverBg.loadImage(this, imgUrl)
        }
        tvName.text = title
        tvDescription.text = description
    }

    override fun onDestroy() {
        mImmersionBar.destroy()
        super.onDestroy()
    }

    abstract fun getPages(): FragmentPagerItems

    companion object {
        @IntDef(EXPANDED, COLLAPSED, MIDDLE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class AppBarState

        const val EXPANDED = 0
        const val COLLAPSED = 1
        const val MIDDLE = 2
    }
}