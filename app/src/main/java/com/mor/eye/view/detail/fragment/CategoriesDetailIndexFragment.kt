package com.mor.eye.view.detail.fragment

import android.os.Bundle
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.CategoryArgumentConstant.Companion.CATEGORY_DEFAULT_ICON
import com.mor.eye.util.other.CategoryArgumentConstant.Companion.CATEGORY_ID
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItem
import org.koin.android.viewmodel.ext.android.viewModel

class CategoriesDetailIndexFragment : DetailIndexBaseFragment() {
    private val model: CategoriesDetailIndexViewModel by viewModel()
    private val defaultUrl by unsafeLazy { arguments!![CATEGORY_DEFAULT_ICON] as String }
    private val id by unsafeLazy { arguments!![CATEGORY_ID] as String }

    override fun observeViewModel() {
        model.uiEvent.observeK(this) { uiResource ->
            when (uiResource?.status) {
                Status.SUCCESS -> swipeRefreshLatch.refreshing = false
                Status.ERROR -> {
                    swipeRefreshLatch.refreshing = false
                    uiResource.exception?.let { showErrorToast(it) }
                }
                Status.REFRESHING -> swipeRefreshLatch.refreshing = true
                Status.LOADING_MORE -> controller.showFooter()
                Status.NO_MORE -> {
                    swipeRefreshLatch.refreshing = false
                    controller.showEnd()
                }
                Status.NO_DATA -> {
                    swipeRefreshLatch.refreshing = false
                    controller.showEmpty()
                }
            }
        }

        model.uiLoadData.observeK(this) {
            controller.update(it!!)
            startPostponedEnterTransition()
        }
        model.uiLoadMoreData.observeK(this) {
            controller.addAll(it!!)
        }
    }

    override fun lazyFetchData() {
        model.position = FragmentPagerItem.getPosition(arguments)
        controller.setDefaultUrl(defaultUrl)
        model.id = id
        model.refresh()
    }

    override fun setViewModel(): AbstractViewModel = model

    companion object {

        fun newInstance(id: String, iconUrl: String): CategoriesDetailIndexFragment {
            val fragment = CategoriesDetailIndexFragment()
            fragment.arguments = CategoriesDetailIndexFragment.arguments(id, iconUrl)
            return fragment
        }

        fun arguments(id: String, iconUrl: String): Bundle {
            return Bundler()
                    .putString(CATEGORY_ID, id)
                    .putString(CATEGORY_DEFAULT_ICON, iconUrl)
                    .get()
        }
    }
}