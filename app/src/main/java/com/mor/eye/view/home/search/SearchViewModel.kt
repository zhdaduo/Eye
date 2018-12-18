package com.mor.eye.view.home.search

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.local.RecentSearch
import com.mor.eye.ui.Status
import com.mor.eye.ui.UiResource
import com.mor.eye.util.ktx.SingleLiveEvent
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class SearchViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    val uiEvent = SingleLiveEvent<UiResource>()
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()
    private val _uiListKeyWordData = MutableLiveData<List<String>>()
    private val _uiListHistoryData = MutableLiveData<List<String>>()


    private var enableScrollToEnd = true
    private var nextPageUrl: String? = null

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
                    .doOnSubscribe { uiEvent.postValue(UiResource(Status.REFRESHING)) }
                    .with(scheduler)
                    .subscribe(
                            { findBean ->
                                saveKeyHistory(query)
                                if (findBean == null) uiEvent.postValue(UiResource(Status.NO_DATA))
                                else {
                                    _uiLoadData.postValue(findBean.itemList)
                                }
                                if (findBean.nextPageUrl == null) {
                                    enableScrollToEnd = false
                                } else {
                                    nextPageUrl = findBean.nextPageUrl
                                }
                            }, {}
                    )
        }
    }

    fun onListScrolledToEnd() {
        if (enableScrollToEnd) {
            launch {
                repository.getLoadMoreData(nextPageUrl!!)
                        .doOnSubscribe { uiEvent.postValue(UiResource(Status.LOADING_MORE)) }
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
                                        nextPageUrl = findBean.nextPageUrl
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