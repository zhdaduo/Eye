package com.mor.eye.view.home.recommend

import com.airbnb.epoxy.EpoxyController
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.mor.eye.view.base.BaseTabFragment
import org.koin.android.viewmodel.ext.android.viewModel

class RecommendFragment : BaseTabFragment() {
    private val model: RecommendViewModel by viewModel()
    private val controller: RecommendEpoxyController by unsafeLazy { RecommendEpoxyController(requireContext(), callbacks) }

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
            when (it?.isTag) {
                "true" -> controller.addLoadData(it.items)
                "false" -> controller.addLoadMoreData(it.items)
            }
        }
    }

    override fun lazyFetchData() {
        model.refresh()
    }
}