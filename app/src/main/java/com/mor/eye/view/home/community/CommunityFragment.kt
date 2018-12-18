package com.mor.eye.view.home.community

import com.airbnb.epoxy.EpoxyController
import com.mor.eye.R
import com.mor.eye.ui.video.ScrollCalculatorHelper
import com.mor.eye.ui.Status
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.ktx.doOnScroll
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.mor.eye.view.base.BaseTabFragment
import com.shuyu.gsyvideoplayer.GSYVideoManager
import org.koin.android.viewmodel.ext.android.viewModel

class CommunityFragment : BaseTabFragment() {
    private lateinit var scrollCalculatorHelper: ScrollCalculatorHelper

    private val model: CommunityViewModel by viewModel()
    private val controller: CommunityEpoxyController by unsafeLazy { CommunityEpoxyController( requireContext(), callbacks) }

    override fun init() {
        super.init()
        scrollCalculatorHelper = ScrollCalculatorHelper(R.id.video_item_player, 0, DisplayUtils.getScreenHeight(requireActivity()))

        recyclerView.doOnScroll(
                { recyclerView, _, _ ->
                    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()
                    val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                    scrollCalculatorHelper.onScroll(recyclerView!!, firstVisibleItem, lastVisibleItem, lastVisibleItem - firstVisibleItem)
                },
                { recyclerView, newState ->
                    scrollCalculatorHelper.onScrollStateChanged(recyclerView!!, newState)
                }
        )
    }

    override fun setViewModel(): AbstractViewModel = model

    override fun setController(): EpoxyController = controller

    override fun observeViewModel() {
        model.uiEvent.observeK(this) { uiResources ->
            when (uiResources?.status) {
                Status.SUCCESS -> swipeRefreshLatch.refreshing = false
                Status.ERROR -> {
                    swipeRefreshLatch.refreshing = false
                    uiResources.exception?.let { showErrorToast(it) }
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
        model.refresh()
    }

    override fun onPause() {
        super.onPause()
        GSYVideoManager.onPause()
    }

    override fun onResume() {
        super.onResume()
        GSYVideoManager.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoManager.releaseAllVideos()
    }
}