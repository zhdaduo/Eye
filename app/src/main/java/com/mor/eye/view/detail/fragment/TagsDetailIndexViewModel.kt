package com.mor.eye.view.detail.fragment

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.ui.Status
import com.mor.eye.ui.UiResource
import com.mor.eye.util.StringUtils
import com.mor.eye.util.ktx.SingleLiveEvent
import com.mor.eye.util.other.DataSourceConstant
import com.mor.eye.util.other.TagTabConstant.Companion.TAG_DYNAMICS
import com.mor.eye.util.other.TagTabConstant.Companion.TAG_VIDEOS
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.AbstractViewModel

class TagsDetailIndexViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : AbstractViewModel() {
    val uiEvent = SingleLiveEvent<UiResource>()
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()
    private val _uiLoadMoreData = MutableLiveData<List<ItemListBean>>()

    private var stringHashMap = HashMap<String, String?>()
    private var enableScrollToEnd = true

    var tabType = TAG_VIDEOS
        set(value) {
            if (value != field) {
                field = value
            }
        }

    var id = "0"
        set(value) {
            if (value != field) {
                field = value
            }
        }

    val uiLoadData: LiveData<List<ItemListBean>>
        get() = _uiLoadData
    val uiLoadMoreData: LiveData<List<ItemListBean>>
        get() = _uiLoadMoreData

    override fun refresh() {
        launch {
            repository.getTagDetailData(tabType, id)
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
                                    validatePageUrl(tabType, findBean.nextPageUrl)
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
                repository.getMoreTagDetailData(tabType, id, stringHashMap)
                        .doOnSubscribe { uiEvent.postValue(UiResource(Status.LOADING_MORE)) }
                        .with(scheduler)
                        .subscribe(
                                { findBean ->
                                    if (findBean.count == 0) uiEvent.postValue(UiResource(Status.NO_MORE))
                                    else {
                                        _uiLoadMoreData.postValue(findBean.itemList)
                                        uiEvent.postValue(UiResource(Status.SUCCESS))
                                    }
                                    if (findBean.nextPageUrl == null) {
                                        enableScrollToEnd = false
                                    } else {
                                        validatePageUrl(tabType, findBean.nextPageUrl)
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

    private fun validatePageUrl(tabType: String, url: String) = when (tabType) {
        TAG_VIDEOS, TAG_DYNAMICS -> {
            stringHashMap[DataSourceConstant.START] = StringUtils.urlRequest(url)[DataSourceConstant.START]
            stringHashMap[DataSourceConstant.NUM] = StringUtils.urlRequest(url)[DataSourceConstant.NUM]
        }
        else -> throw IllegalArgumentException("tag url parse invalid")
    }
}