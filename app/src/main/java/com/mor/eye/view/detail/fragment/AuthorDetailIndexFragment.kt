package com.mor.eye.view.detail.fragment

import android.os.Bundle
import com.mor.eye.ui.Status
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.AuthorArgumentConstant.Companion.AUTHOR_ID
import com.mor.eye.util.other.AuthorArgumentConstant.Companion.AUTHOR_TAB_TYPE
import com.mor.eye.util.other.AuthorArgumentConstant.Companion.AUTHOR_TYPE
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.AbstractViewModel
import com.ogaclejapan.smarttablayout.utils.v4.Bundler
import com.shuyu.gsyvideoplayer.GSYVideoManager
import org.koin.android.viewmodel.ext.android.viewModel

class AuthorDetailIndexFragment : DetailIndexBaseFragment() {
    private val model: AuthorDetailIndexViewModel by viewModel()
    private val tabType by unsafeLazy { arguments!![AUTHOR_TAB_TYPE] as Int }
    private val id by unsafeLazy { arguments!![AUTHOR_ID] as String }
    private val userType by unsafeLazy { arguments!![AUTHOR_TYPE] as String }

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

    override fun setViewModel(): AbstractViewModel = model

    override fun lazyFetchData() {
        model.tabType = tabType
        model.id = id
        model.userType = userType
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

    companion object {

        fun newInstance(id: String, userType: String, tabType: Int): AuthorDetailIndexFragment {
            val fragment = AuthorDetailIndexFragment()
            fragment.arguments = AuthorDetailIndexFragment.arguments(id, userType, tabType)
            return fragment
        }

        fun arguments(id: String, userType: String, tabType: Int): Bundle {
            return Bundler()
                    .putString(AUTHOR_ID, id)
                    .putString(AUTHOR_TYPE, userType)
                    .putInt(AUTHOR_TAB_TYPE, tabType)
                    .get()
        }
    }
}