package com.mor.eye.view.home.daily

import com.airbnb.epoxy.EpoxyController
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.mor.eye.view.base.BaseTabFragment
import org.koin.android.viewmodel.ext.android.viewModel

class DailyFragment : BaseTabFragment() {
    private val model: DailyViewModel by viewModel()
    private val controller: DailyEpoxyController by unsafeLazy { DailyEpoxyController(requireContext(), callbacks) }

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
                Status.NO_MORE -> controller.showEnd()
                Status.NO_DATA -> controller.showEmpty()
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
        model.refresh()
    }
}