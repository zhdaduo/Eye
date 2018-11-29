package com.mor.eye.view.home.search

import android.content.Context
import android.view.inputmethod.EditorInfo
import com.mor.eye.R
import com.mor.eye.queryItem
import com.mor.eye.ui.EndlessRecyclerViewScrollListener
import com.mor.eye.ui.Status
import com.mor.eye.util.epoxy.withModels
import com.mor.eye.util.ktx.observeK
import com.mor.eye.util.other.*
import com.mor.eye.view.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.android.viewmodel.ext.android.viewModel

class SearchActivity : BaseActivity() {
    private val model: SearchViewModel by viewModel()
    private val controller: SearchResultEpoxyController by unsafeLazy { SearchResultEpoxyController(this@SearchActivity, callbacks) }
    private val recentController: RecentSearchController by unsafeLazy {
        RecentSearchController { query ->
            showResultPage()
            search_edit_text.setText(query)
            model.getQueryData(query)
        }
    }
    private var recentList: MutableList<String> = ArrayList()
    override fun getLayout(): Int = R.layout.activity_search

    override fun init() {
        overridePendingTransition(R.anim.screen_top_in, R.anim.screen_null)
        model.getSearchHotKeyWord()
        model.getKeyHistory()
        initRecyclerView()
        initListener()
        observeViewModel()
    }

    private fun initRecyclerView() {
        result_search_rv.apply {
            itemAnimator = null
            adapter = controller.adapter
            addOnScrollListener(EndlessRecyclerViewScrollListener(layoutManager) { _, _ -> model.onListScrolledToEnd() })
        }
        search_history_rv.setController(recentController)
    }

    private fun initListener() {
        search_edit_text.apply {
            setOnDrawableClearListener(object : SearchEditText.OnDrawableClearListener {
                override fun onDrawableClearClick() {
                    showOriginPage()
                }
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    model.getQueryData(search_edit_text.text.toString())
                    showResultPage()
                }
                false
            }
        }
        delete_history.setOnClickListener {
            model.deleteKeyHistory()
            recentList.clear()
            updateRecentController()
            history_fl.remove()
        }
        cancel_main.setOnClickListener {
            this@SearchActivity.finish()
            overridePendingTransition(R.anim.screen_null, R.anim.screen_top_out)
        }
    }

    private fun showOriginPage() {
        history_fl.show()
        layout_origin.show()
        result_search_rv.remove()
        result_search_rv.adapter = null
        search_edit_text.setText("")
    }

    private fun showResultPage() {
        hideKeyboard()
        history_fl.remove()
        layout_origin.remove()
        result_search_rv.show()
    }

    private fun observeViewModel() {
        model.uiEvent.observeK(this) {
            when (it?.status) {
                Status.NO_DATA -> controller.showEmpty()
                Status.NO_MORE -> controller.showEnd()
            }
        }
        model.uiListKeyWordData.observeK(this) {
            hot_search_rv.withModels {
                it?.forEach { query ->
                    queryItem {
                        id(query)
                        queryText(query)
                        queryClick { _ ->
                            showResultPage()
                            search_edit_text.setText(query)
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
                history_fl.remove()
            } else {
                recentList.clear()
                recentList.addAll(it)
                updateRecentController()
            }
        }
        model.uiLoadData.observeK(this) {
            controller.addLoadData(it!!)
        }
    }

    private fun updateRecentController() {
        recentController.setData(recentList)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideKeyboard()
        overridePendingTransition(R.anim.screen_null, R.anim.screen_top_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        result_search_rv.adapter = null
        hot_search_rv.adapter = null
        search_history_rv.adapter = null
    }

    companion object {
        fun open(context: Context) = context.startKActivity(SearchActivity::class) {}
    }
}
