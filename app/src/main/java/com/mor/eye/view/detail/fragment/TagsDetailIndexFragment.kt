package com.mor.eye.view.detail.fragment

import android.os.Bundle
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.TagArgumentConstant.Companion.TAG_ID
import com.mor.eye.util.other.TagArgumentConstant.Companion.TAG_TAB_TYPE
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import org.koin.android.viewmodel.ext.android.viewModel

class TagsDetailIndexFragment : DetailIndexBaseFragment() {
    private val model: TagsDetailIndexViewModel by viewModel()
    private val tabType by unsafeLazy { arguments!![TAG_TAB_TYPE] as Int }
    private val id by unsafeLazy { arguments!![TAG_ID] as String }

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
        model.id = id
        model.tabType = tabType
        model.refresh()
    }

    override fun setViewModel(): AbstractViewModel = model

    companion object {

        fun newInstance(id: String, tabType: Int): TagsDetailIndexFragment {
            val fragment = TagsDetailIndexFragment()
            fragment.arguments = arguments(id, tabType)
            return fragment
        }

        fun arguments(id: String, tabType: Int): Bundle {
            return Bundler()
                    .putString(TAG_ID, id)
                    .putInt(TAG_TAB_TYPE, tabType)
                    .get()
        }
    }
}