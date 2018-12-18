package com.mor.eye.view.detail.activity

import android.content.Context
import android.os.Bundle
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.TagArgumentConstant.Companion.TAG_ID
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.detail.fragment.TagsDetailIndexFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import kotlinx.android.synthetic.main.activity_base_detail.*
import org.koin.android.viewmodel.ext.android.viewModel

class TagsDetailActivity : DetailBaseActivity() {
    private val model: TagsDetailViewModel by viewModel()
    private val pageItems by unsafeLazy { FragmentPagerItems(this) }
    private val id by unsafeLazy { intent.getStringExtra(TAG_ID) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model.id = id
        model.refresh()
        model.uiLoadData.observeK(this) {
            val tagInfoBean = it?.tagInfo!!
            val description = buildSpannedString {
                append(tagInfoBean.tagVideoCount.toString())
                append(" ")
                append("作品")
                append("/")
                append(tagInfoBean.tagFollowCount.toString())
                append(" ")
                append("关注")
                append("/")
                append(tagInfoBean.tagDynamicCount.toString())
                append(" ")
                append("动态")
            }
            initUi(tagInfoBean.follow.isFollowed, tagInfoBean.name, tagInfoBean.headerImage, description.toString())

            pageItems.apply {
                it.tabInfo.tabList.let { tabs ->
                    for (i in 0 until tabs.size) {
                        val uid = if (tabs[i].id < 0) id else tabs[i].id.toString()
                        add(FragmentPagerItem.of(tabs[i].name, TagsDetailIndexFragment::class.java, TagsDetailIndexFragment.arguments(uid, i)))
                    }
                }
                view_pager.adapter = FragmentPagerItemAdapter(supportFragmentManager, pageItems)
                tab_view_pager.setViewPager(view_pager)
            }
        }
    }

    override fun getPages(): FragmentPagerItems = pageItems

    companion object {
        fun open(context: Context, id: String) = context.startKActivity(TagsDetailActivity::class) {
            putExtra(TAG_ID, id)
        }
    }
}
