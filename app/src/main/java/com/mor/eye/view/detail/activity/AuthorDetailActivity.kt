package com.mor.eye.view.detail.activity

import android.content.Context
import com.mor.eye.R
import com.mor.eye.repository.data.AuthorDetailBean
import com.mor.eye.util.StringUtils
import com.mor.eye.util.glide.loadImage
import com.mor.eye.util.glide.loadLocalImage
import com.mor.eye.util.glide.loadWithCircle
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.AuthorArgumentConstant.Companion.AUTHOR_ID
import com.mor.eye.util.other.AuthorArgumentConstant.Companion.AUTHOR_TYPE
import com.mor.eye.util.other.AuthorTabConstant.Companion.AUTHOR_WORKS
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.detail.fragment.AuthorDetailIndexFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_author_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class AuthorDetailActivity : DetailBaseActivity() {
    private val model: AuthorDetailViewModel by viewModel()
    private val id by unsafeLazy { intent.getStringExtra(AUTHOR_ID) }
    private val userType by unsafeLazy { intent.getStringExtra(AUTHOR_TYPE) }
    private val pageItems by unsafeLazy { FragmentPagerItems(this) }

    override fun getLayout(): Int = R.layout.activity_author_detail

    override fun observeViewModel() {
        model.id = id
        model.userType = userType
        model.refresh()
        model.uiLoadData.observeK(this) { uiLoadData ->
            uiLoadData?.pgcInfo?.let {
                initAuthorUI(it)
            }
            uiLoadData?.userInfo?.let {
                initAuthorUI(it)
            }

            pageItems.apply {
                uiLoadData?.tabInfo?.tabList?.let { tabs ->
                    for (i in 0 until tabs.size) {
                        val uid = if (tabs[i].name == AUTHOR_WORKS) StringUtils.urlRequest(tabs.first { it.name == AUTHOR_WORKS }.apiUrl)["uid"]!! else id
                        add(FragmentPagerItem.of(tabs[i].name, AuthorDetailIndexFragment::class.java, AuthorDetailIndexFragment.arguments(uid, userType, tabs[i].name)))
                    }
                }

                view_pager.adapter = FragmentPagerItemAdapter(supportFragmentManager, pageItems)
                view_pager.setCurrentItem(uiLoadData?.tabInfo?.defaultIdx!!, false)
                tab_view_pager.setViewPager(view_pager)
            }
        }
    }

    override fun getPages(): FragmentPagerItems {
        initToolbar()
        return pageItems
    }

    private fun initAuthorUI(info: AuthorDetailBean.PgcInfoBean) {
        tv_name.text = info.name
        tv_bold_title.text = info.name
        tv_focus_num.text = info.brief
        tv_video_num.text = info.videoCount.toString()
        tv_like_num.text = info.collectCount.toString()
        tv_share_num.text = info.shareCount.toString()
        tv_description.text = info.description
        iv_head.loadWithCircle(this, info.icon)
        if (info.cover.isNullOrBlank()) {
            iv_cover_bg.loadLocalImage(this, R.mipmap.cover_default)
        } else {
            iv_cover_bg.loadImage(this, info.cover)
        }
    }

    private fun initAuthorUI(info: AuthorDetailBean.UserInfoBean) {
        tv_name.text = info.name
        tv_bold_title.text = info.name
        tv_focus_num.text = info.brief
        tv_video_num.text = info.videoCount.toString()
        tv_like_num.text = info.collectCount.toString()
        tv_share_num.text = info.shareCount.toString()
        tv_description.text = info.description
        iv_head.loadWithCircle(this, info.icon)
        if (info.cover.isNullOrBlank()) {
            iv_cover_bg.loadLocalImage(this, R.mipmap.cover_default)
        } else {
            iv_cover_bg.loadImage(this, info.cover)
        }
    }

    companion object {
        fun open(context: Context, id: String, userType: String) = context.startKActivity(AuthorDetailActivity::class) {
            putExtra(AUTHOR_ID, id)
            putExtra(AUTHOR_TYPE, userType)
        }
    }
}
