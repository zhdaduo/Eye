package com.mor.eye.view.detail.activity

import android.content.Context
import com.mor.eye.R
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.CategoryArgumentConstant.Companion.CATEGORY_DEFAULT_ICON
import com.mor.eye.util.other.CategoryArgumentConstant.Companion.CATEGORY_ID
import com.mor.eye.util.other.startKActivity
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.detail.fragment.CategoriesDetailIndexFragment
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems
import org.koin.android.viewmodel.ext.android.viewModel

class CategoriesDetailActivity : DetailBaseActivity() {
    private val model: CategoriesDetailViewModel by viewModel()
    private val pageItems by unsafeLazy { FragmentPagerItems(this) }

    private val iconUrl by unsafeLazy { intent.getStringExtra(CATEGORY_DEFAULT_ICON) }
    private val id by unsafeLazy { intent.getStringExtra(CATEGORY_ID) }

    override fun getPages(): FragmentPagerItems {
        val tabs = resources.getStringArray(R.array.categories_tabs)

        pageItems.apply {
            for (i in 0 until tabs.size) {
                add(FragmentPagerItem.of(tabs[i], CategoriesDetailIndexFragment::class.java, CategoriesDetailIndexFragment.arguments(id, iconUrl)))
            }
        }
        return pageItems
    }

    override fun observeViewModel() {
        model.id = id
        model.refresh()

        model.uiLoadData.observeK(this) { uiLoadData ->
            val categoryInfoBean = uiLoadData?.categoryInfo!!
            initUi(categoryInfoBean.follow.isFollowed, categoryInfoBean.name, categoryInfoBean.headerImage, categoryInfoBean.description)
        }
    }

    companion object {
        fun open(context: Context, id: String, iconUrl: String) = context.startKActivity(CategoriesDetailActivity::class) {
            putExtra(CATEGORY_ID, id)
            putExtra(CATEGORY_DEFAULT_ICON, iconUrl)
        }
    }
}
