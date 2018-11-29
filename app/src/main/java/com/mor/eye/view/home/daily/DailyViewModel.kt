package com.mor.eye.view.home.daily

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.ui.Status
import com.mor.eye.ui.UiResource
import com.mor.eye.util.StringUtils
import com.mor.eye.util.ktx.SingleLiveEvent
import com.mor.eye.util.other.DataSourceConstant
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.AbstractViewModel

class DailyViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : AbstractViewModel() {
    val uiEvent = SingleLiveEvent<UiResource>()
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()
    private val _uiLoadMoreData = MutableLiveData<List<ItemListBean>>()

    var stringHashMap = HashMap<String, String?>()
    private val id = -3
    private var enableScrollToEnd = true

    val uiLoadData: LiveData<List<ItemListBean>>
        get() = _uiLoadData
    val uiLoadMoreData: LiveData<List<ItemListBean>>
        get() = _uiLoadMoreData

    override fun refresh() {
        launch {
            repository.getCommonTabData(id)
                    .doOnSubscribe { uiEvent.postValue(UiResource(Status.REFRESHING)) }
                    .with(scheduler)
                    .subscribe(
                            { findBean ->
                                if (findBean.count == 0) uiEvent.postValue(UiResource(Status.NO_DATA))
                                else {
                                    _uiLoadData.postValue(findBean.itemList)
                                    uiEvent.postValue(UiResource(Status.SUCCESS))
                                }
                                if (findBean.nextPageUrl == null) {
                                    enableScrollToEnd = false
                                } else {
                                    findBean.nextPageUrl.let { url ->
                                        stringHashMap[DataSourceConstant.DAY_DATE] = StringUtils.urlRequest(url)["date"]
                                        stringHashMap[DataSourceConstant.DAY_NUM] = StringUtils.urlRequest(url)["num"]
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
                repository.getMoreCommonTabData(stringHashMap, id)
                        .doOnSubscribe { uiEvent.postValue(UiResource(Status.LOADING_MORE)) }
                        .with(scheduler)
                        .subscribe(
                                { findBean ->
                                    if (findBean.count == 0) {
                                        uiEvent.postValue(UiResource(Status.NO_MORE))
                                    } else {
                                        _uiLoadMoreData.postValue(findBean.itemList)
                                        uiEvent.postValue(UiResource(Status.SUCCESS))
                                    }
                                    if (findBean.nextPageUrl == null) {
                                        enableScrollToEnd = false
                                    } else {
                                        findBean.nextPageUrl.let { url ->
                                            stringHashMap[DataSourceConstant.DAY_DATE] = StringUtils.urlRequest(url)["date"]
                                            stringHashMap[DataSourceConstant.DAY_NUM] = StringUtils.urlRequest(url)["num"]
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
}