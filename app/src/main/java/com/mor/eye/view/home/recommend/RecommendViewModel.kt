package com.mor.eye.view.home.recommend

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.ui.Status
import com.mor.eye.ui.UiResource
import com.mor.eye.util.StringUtils
import com.mor.eye.util.ktx.SingleLiveEvent
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.AbstractViewModel

class RecommendViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : AbstractViewModel() {
    val uiEvent = SingleLiveEvent<UiResource>()
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()
    private val _uiLoadMoreData = MutableLiveData<UILoadMoreData>()

    private var page: String? = null
    private var isTag: String? = null
    private var adIndex: String? = null

    private val id = -2
    private var enableScrollToEnd = true

    val uiLoadData: LiveData<List<ItemListBean>>
        get() = _uiLoadData
    val uiLoadMoreData: LiveData<UILoadMoreData>
        get() = _uiLoadMoreData

    override fun refresh() {
        launch {
            repository.getRecommendData()
                    .doOnSubscribe { uiEvent.postValue(UiResource(Status.REFRESHING)) }
                    .with(scheduler)
                    .subscribe(
                            { findBean ->
                                if (findBean.count == 0 || findBean == null) uiEvent.postValue(UiResource(Status.NO_DATA))
                                else {
                                    _uiLoadData.postValue(findBean.itemList)
                                    uiEvent.postValue(UiResource(Status.SUCCESS))
                                }
                                if (findBean.nextPageUrl.isNullOrBlank()) {
                                    enableScrollToEnd = false
                                } else {
                                    findBean.nextPageUrl.let { url ->
                                        page = StringUtils.urlRequest(url!!)["page"]
                                        isTag = StringUtils.urlRequest(url)["isTag"]
                                        adIndex = StringUtils.urlRequest(url)["adIndex"]
                                    }
                                }
                            },
                            { t ->
                                uiEvent.postValue(UiResource(Status.ERROR, t))
                            }
                    )
        }
    }

    override fun onListScrolledToEnd() {
        if (enableScrollToEnd) {
            launch {
                repository.getMoreRecommendData(page!!, isTag!!, adIndex!!)
                        .doOnSubscribe { uiEvent.postValue(UiResource(Status.LOADING_MORE)) }
                        .with(scheduler)
                        .subscribe(
                                { findBean ->
                                    uiEvent.postValue(UiResource(Status.SUCCESS))
                                    if (findBean == null) {
                                        uiEvent.postValue(UiResource(Status.NO_MORE))
                                    } else {
                                        _uiLoadMoreData.postValue(UILoadMoreData(findBean.itemList, isTag!!))
                                    }
                                    if (findBean.nextPageUrl == null) {
                                        enableScrollToEnd = false
                                    } else {
                                        findBean.nextPageUrl.let { url ->
                                            page = StringUtils.urlRequest(url)["page"]
                                            isTag = StringUtils.urlRequest(url)["isTag"]
                                            adIndex = StringUtils.urlRequest(url)["adIndex"].orEmpty()
                                        }
                                    }
                                },
                                { t ->
                                    uiEvent.postValue(UiResource(Status.ERROR, t))
                                }
                        )
            }
        } else {
            uiEvent.postValue(UiResource(Status.NO_MORE))
        }
    }

    data class UILoadMoreData(val items: List<ItemListBean>, val isTag: String)
}