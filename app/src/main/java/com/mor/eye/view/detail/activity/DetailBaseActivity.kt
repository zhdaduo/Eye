package com.mor.eye.view.detail.activity

import android.support.annotation.IntDef
import android.support.v4.view.ViewPager
import com.mor.eye.R
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.glide.loadLocalImage
import com.mor.eye.util.other.remove
import com.mor.eye.util.other.show
import com.mor.eye.view.base.BaseActivity
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_base_detail.*

abstract class DetailBaseActivity : BaseActivity() {
    @AppBarState
    private var mState: Int? = null
    lateinit var viewPager: ViewPager
    lateinit var pageAdapter: FragmentPagerItemAdapter
    override fun getLayout(): Int = R.layout.activity_base_detail

    override fun init() {
        setupToolbar()
        initTab()
        initListener()
        observeViewModel()
    }

    private fun initListener() {
        app_bar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {

                if (mState != EXPANDED) {
                    mState = EXPANDED // 修改状态标记为展开
                    tv_bold_title.remove()
                    toolbar?.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
                }
            } else if (Math.abs(verticalOffset) >= appBarLayout.totalScrollRange * 0.8) {
                if (mState != COLLAPSED) {
                    mState = COLLAPSED // 修改状态标记为折叠
                    tv_bold_title.show()
                    toolbar?.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_black)
                }
            } else {
                if (mState != MIDDLE) {
                    if (mState == COLLAPSED) {
                        tv_bold_title.remove()
                        toolbar?.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
                    }
                    mState = MIDDLE // 修改状态标记为中间
                }
            }
        }
    }

    private fun initTab() {
        viewPager = view_pager
        pageAdapter = FragmentPagerItemAdapter(this@DetailBaseActivity.supportFragmentManager, getPages())
        view_pager.adapter = pageAdapter
        tab_view_pager.setViewPager(view_pager)
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        toolbar?.navigationIcon = DisplayUtils.getDrawable(this, R.drawable.ic_arrow_left_white)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
    }

    fun initUi(isFollowed: Boolean, title: String, imgUrl: String, description: String) {
        if (isFollowed) btn_focus.remove() else btn_focus.show()
        tv_bold_title.text = title
        if (imgUrl.isNullOrBlank()) {
            iv_cover_bg.loadLocalImage(this, R.mipmap.cover_default)
        } else {
            iv_cover_bg.loadImage(this, imgUrl)
        }
        tv_name.text = title
        tv_description.text = description
    }

    abstract fun getPages(): FragmentPagerItems
    abstract fun observeViewModel()

    companion object {
        @IntDef(EXPANDED, COLLAPSED, MIDDLE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class AppBarState

        const val EXPANDED = 0
        const val COLLAPSED = 1
        const val MIDDLE = 2
    }
}