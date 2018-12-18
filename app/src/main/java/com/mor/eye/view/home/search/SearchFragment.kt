package com.mor.eye.view.home.search

import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.TextView
import com.airbnb.epoxy.EpoxyRecyclerView
import com.mor.eye.R
import com.mor.eye.queryItem
import com.mor.eye.ui.SearchEditText
import com.mor.eye.ui.Status
import com.mor.eye.ui.recyclerview.EndlessRecyclerViewScrollListener
import com.mor.eye.ui.recyclerview.ProgressTimeLatch
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.*
import com.mor.eye.view.base.BaseFragment
import me.yokeyword.fragmentation.anim.FragmentAnimator
import org.koin.android.viewmodel.ext.android.viewModel

class SearchFragment: BaseFragment() {

    private val model: SearchViewModel by viewModel()
    private var controller: SearchResultEpoxyController? = null
    private var recentController: RecentSearchController? = null

    private lateinit var searchEditText: SearchEditText
    private lateinit var tvCancel: TextView
    private lateinit var rvSearchResult: EpoxyRecyclerView
    private lateinit var rvSearchHistory: EpoxyRecyclerView
    private lateinit var rvSearchHot: EpoxyRecyclerView
    private lateinit var originLayout: CoordinatorLayout
    private lateinit var historyLayout: FrameLayout
    private lateinit var tvDeleteHistory: TextView
    private lateinit var refreshLayout: SwipeRefreshLayout
    lateinit var swipeRefreshLatch: ProgressTimeLatch

    private var recentList: MutableList<String> = ArrayList()

    override fun getLayoutRes(): Int = R.layout.activity_search

    override fun getToolBarResId(): Int = R.layout.toolbar_search_title

    override fun initView(rootView: View) {
        rvSearchResult = rootView.findViewById(R.id.result_search_rv)
        rvSearchHistory = rootView.findViewById(R.id.search_history_rv)
        rvSearchHot = rootView.findViewById(R.id.hot_search_rv)
        originLayout = rootView.findViewById(R.id.layout_origin)
        historyLayout = rootView.findViewById(R.id.history_fl)
        tvDeleteHistory = rootView.findViewById(R.id.delete_history)
        searchEditText = rootView.findViewById( R.id.search_edit_text)
        tvCancel = rootView.findViewById( R.id.cancel_main)
        refreshLayout = rootView.findViewById(R.id.refresh_layout)
        controller = SearchResultEpoxyController(requireContext(), callbacks)
        recentController =  RecentSearchController { query ->
            showResultPage()
            searchEditText.setText(query)
            model.getQueryData(query)
        }
        initRecyclerView()
        initListener()
        initRefreshLayout()
    }

    override fun initLogic() {
        model.getSearchHotKeyWord()
        model.getKeyHistory()
        observeViewModel()
    }

    override fun initToolBar(rootView: View) {
        val toolbar: Toolbar by bindable(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

    private fun initRefreshLayout() {
        swipeRefreshLatch = ProgressTimeLatch(minShowTime = 1350) {
            refreshLayout.isRefreshing = it
        }
        refreshLayout.setOnRefreshListener { model.getQueryData(searchEditText.text.toString()) }
    }

    private fun initRecyclerView() {
        rvSearchResult.apply {
            itemAnimator = null
            adapter = controller?.adapter!!
            addOnScrollListener(EndlessRecyclerViewScrollListener(layoutManager) { _, _ -> model.onListScrolledToEnd() })
        }
        recentController?.let { rvSearchHistory.setController(it) }
    }

    private fun initListener() {
        searchEditText.apply {
            setOnDrawableClearListener(object : SearchEditText.OnDrawableClearListener {
                override fun onDrawableClearClick() {
                    showOriginPage()
                }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    model.getQueryData(searchEditText.text.toString())
                    showResultPage()
                }
                false
            }
        }
        tvDeleteHistory.setOnClickListener {
            model.deleteKeyHistory()
            recentList.clear()
            updateRecentController()
            historyLayout.remove()
        }
        tvCancel.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun showOriginPage() {
        historyLayout.show()
        originLayout.show()
        rvSearchResult.remove()
        refreshLayout.remove()
        rvSearchResult.adapter = null
        searchEditText.setText("")
    }

    private fun showResultPage() {
        hideKeyboard()
        historyLayout.remove()
        originLayout.remove()
        rvSearchResult.show()
        refreshLayout.show()
    }

    private fun observeViewModel() {
        model.uiEvent.observeK(this) { uiResource ->
            when (uiResource?.status) {
                Status.SUCCESS -> swipeRefreshLatch.refreshing = false
                Status.ERROR -> {
                    swipeRefreshLatch.refreshing = false
                    uiResource.exception?.let { showErrorToast(it) }
                }
                Status.REFRESHING -> swipeRefreshLatch.refreshing = true
                Status.LOADING_MORE -> controller?.showFooter()
                Status.NO_MORE -> {
                    swipeRefreshLatch.refreshing = false
                    controller?.showEnd()
                }
                Status.NO_DATA -> {
                    swipeRefreshLatch.refreshing = false
                    controller?.showEmpty()
                }
            }
        }
        model.uiListKeyWordData.observeK(this) { stringList ->
            rvSearchHot.withModels {
                stringList?.forEach { query ->
                    queryItem {
                        id(query)
                        queryText(query)
                        queryClick { _ ->
                            showResultPage()
                            searchEditText.setText(query)
                            model.getQueryData(query)
                            if (!recentList.contains(query)) recentList.add(query)
                            updateRecentController()
                        }
                    }
                }
            }
        }
        model.uiListHistoryData.observeK(this) {
            if (it!!.isEmpty()) {
                historyLayout.remove()
            } else {
                recentList.clear()
                recentList.addAll(it)
                updateRecentController()
            }
        }
        model.uiLoadData.observeK(this) {
            controller?.addLoadData(it!!)
        }
    }

    private fun updateRecentController() {
        recentController?.setData(recentList)
    }

    override fun showToolBar(): Boolean {
        return true
    }

    override fun onCreateFragmentAnimator(): FragmentAnimator {
        return FragmentAnimator(R.anim.screen_top_in, R.anim.screen_top_out ,R.anim.screen_null, R.anim.screen_null)
    }

    override fun onBackPressedSupport(): Boolean {
        hideInput()
        return super.onBackPressedSupport()
    }

    override fun onDestroyView() {
        rvSearchResult.recycledViewPool?.clear()
        rvSearchHot.recycledViewPool?.clear()
        rvSearchHistory.recycledViewPool?.clear()
        rvSearchResult.adapter = null
        rvSearchHot.adapter = null
        rvSearchHistory.adapter = null
        controller = null
        super.onDestroyView()
    }

    companion object {
        fun newInstance(): SearchFragment {
            return SearchFragment()
        }
    }
}