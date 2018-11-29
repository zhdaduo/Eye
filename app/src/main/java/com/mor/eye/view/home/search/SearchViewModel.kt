package com.mor.eye.view.home.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.local.RecentSearch
import com.mor.eye.ui.Status
import com.mor.eye.ui.UiResource
import com.mor.eye.util.StringUtils
import com.mor.eye.util.ktx.SingleLiveEvent
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class SearchViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    val uiEvent = SingleLiveEvent<UiResource>()
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()
    private val _uiListKeyWordData = MutableLiveData<List<String>>()
    private val _uiListHistoryData = MutableLiveData<List<String>>()

    private var start: String? = null
    private var num: String? = null
    private var query: String? = null
    private var enableScrollToEnd = true

    val uiLoadData: LiveData<List<ItemListBean>>
        get() = _uiLoadData
    val uiListKeyWordData: LiveData<List<String>>
        get() = _uiListKeyWordData
    val uiListHistoryData: LiveData<List<String>>
        get() = _uiListHistoryData

    fun getSearchHotKeyWord() {
        launch {
            repository.getSearchHotKeyWord()
                    .with(scheduler)
                    .subscribe(
                            { keyList ->
                                _uiListKeyWordData.postValue(keyList)
                            }, {}
                    )
        }
    }

    fun getKeyHistory() {
        launch {
            repository.getRecentSearch()
                    .with(scheduler)
                    .subscribe(
                            { listKey ->
                                _uiListHistoryData.postValue(listKey.map { it.query })
                            }, {}
                    )
        }
    }

    fun deleteKeyHistory() {
        launch {
            scheduler.io().scheduleDirect {
                repository.deleteSearch()
            }
        }
    }

    private fun saveKeyHistory(query: String) {
        launch {
            scheduler.io().scheduleDirect {
                repository.insertSearch(RecentSearch(query))
            }
        }
    }

    fun getQueryData(query: String) {
        launch {
            repository.getQueryData(query)
                    .with(scheduler)
                    .subscribe(
                            { findBean ->
                                saveKeyHistory(query)
                                this.query = query
                                if (findBean == null) uiEvent.postValue(UiResource(Status.NO_DATA))
                                else {
                                    _uiLoadData.postValue(findBean.itemList)
                                }
                                findBean.nextPageUrl?.let { url ->
                                    start = StringUtils.urlRequest(url)["start"]
                                    num = StringUtils.urlRequest(url)["num"]
                                }
                            }, {}
                    )
        }
    }

    fun onListScrolledToEnd() {
        if (enableScrollToEnd) {
            launch {
                repository.getMoreQueryData(start!!, num!!, query!!)
                        .with(scheduler)
                        .subscribe(
                                { findBean ->
                                    uiEvent.postValue(UiResource(Status.SUCCESS))
                                    if (findBean == null) uiEvent.postValue(UiResource(Status.NO_MORE))
                                    else {
                                        _uiLoadData.postValue(findBean.itemList)
                                    }
                                    if (findBean.nextPageUrl == null) {
                                        enableScrollToEnd = false
                                    } else {
                                        findBean.nextPageUrl.let { url ->
                                            start = StringUtils.urlRequest(url)["start"]
                                            num = StringUtils.urlRequest(url)["num"]
                                        }
                                    }
                                },
                                {}
                        )
            }
        } else {
            uiEvent.postValue(UiResource(Status.NO_MORE))
        }
    }
}