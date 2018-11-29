package com.mor.eye.view.base

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.ui.EndlessRecyclerViewScrollListener
import com.mor.eye.ui.ProgressTimeLatch

abstract class BaseTabFragment : LazyFragment() {
    lateinit var recyclerView: EpoxyRecyclerView
    lateinit var layoutManager: LinearLayoutManager
    private lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var swipeRefreshLatch: ProgressTimeLatch
    lateinit var mParentView: View

    override fun initView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mParentView = inflater.inflate(R.layout.fragment_common_tab, container, false).apply {
            recyclerView = findViewById(R.id.recycler_view)
            refreshLayout = findViewById(R.id.refresh_layout)
        }
        return mParentView
    }

    override fun init() {
        initRefreshLayout()
        initRecyclerView()
        observeViewModel()
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
            setRemoveAdapterWhenDetachedFromWindow(true)
            itemAnimator = null
            adapter = setController().adapter
            addOnScrollListener(EndlessRecyclerViewScrollListener(layoutManager) { _, _ -> setViewModel().onListScrolledToEnd() })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.onDetachedFromWindow()
        recyclerView.adapter = null
        recyclerView.recycledViewPool.clear()
        (mParentView.parent as ViewGroup).removeView(mParentView)
    }

    abstract fun setViewModel(): AbstractViewModel

    abstract fun setController(): EpoxyController

    abstract fun observeViewModel()
}