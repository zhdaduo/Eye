package com.mor.eye.view.detail.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.ui.recyclerview.EndlessRecyclerViewScrollListener
import com.mor.eye.ui.recyclerview.ProgressTimeLatch
import com.mor.eye.ui.video.ScrollCalculatorHelper
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.ktx.doOnScroll
import com.mor.eye.util.other.unsafeLazy
import com.mor.eye.view.base.LazyFragment
import com.mor.eye.view.base.AbstractViewModel
import com.mor.eye.view.detail.DetailIndexBaseController

abstract class DetailIndexBaseFragment : LazyFragment() {
    lateinit var recyclerView: EpoxyRecyclerView
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var swipeRefreshLatch: ProgressTimeLatch
    private lateinit var mParentView: View
    private lateinit var scrollCalculatorHelper: ScrollCalculatorHelper

    val controller: DetailIndexBaseController by unsafeLazy { DetailIndexBaseController(requireContext(), callbacks) }

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = inflater.inflate(R.layout.fragment_common_index, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            refreshLayout = findViewById(R.id.refresh_layout)
        }
        return mParentView
    }

    override fun init() {
        initRefreshLayout()
        initRecyclerView()
        observeViewModel()
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

    private fun initRefreshLayout() {
        swipeRefreshLatch = ProgressTimeLatch(minShowTime = 1350) {
            refreshLayout.isRefreshing = it
        }
        refreshLayout.setOnRefreshListener { setViewModel().refresh() }
    }

    private fun initRecyclerView() {
        layoutManager = recyclerView.layoutManager as LinearLayoutManager
        recyclerView.apply {
            itemAnimator = null
            adapter = controller.adapter
            addOnScrollListener(EndlessRecyclerViewScrollListener(layoutManager) { _, _ ->
                setViewModel().onListScrolledToEnd()
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (mParentView.parent as ViewGroup).removeView(mParentView)
    }

    abstract fun setViewModel(): AbstractViewModel

    abstract fun observeViewModel()
}