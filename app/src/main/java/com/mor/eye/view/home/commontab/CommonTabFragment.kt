package com.mor.eye.view.home.commontab

import android.os.Bundle
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.bundleOf
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.CommonTabArgumentConstant.Companion.HOME_TAB_ID
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.mor.eye.view.base.BaseTabFragment
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import org.koin.android.viewmodel.ext.android.viewModel

class CommonTabFragment : BaseTabFragment() {
    private val model: CommonTabViewModel by viewModel()
    private val controller: CommonTabEpoxyController by unsafeLazy { CommonTabEpoxyController(requireContext(), callbacks) }
    private val categoryType by unsafeLazy { arguments!![HOME_TAB_ID] as Int }

    override fun setViewModel(): AbstractViewModel = model

    override fun setController(): EpoxyController = controller

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
        model.categoryType = categoryType
        model.refresh()
    }

    companion object {
        private const val ARG_ORDER = "CategoryOrder"

        fun newInstance(order: Int): CommonTabFragment {
            val fragment = CommonTabFragment()
            fragment.arguments = bundleOf(ARG_ORDER to order)
            return fragment
        }

        fun arguments(id: Int): Bundle {
            return Bundler()
                    .putInt(HOME_TAB_ID, id)
                    .get()
        }
    }
}