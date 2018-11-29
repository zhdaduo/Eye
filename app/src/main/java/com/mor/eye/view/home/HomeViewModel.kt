package com.mor.eye.view.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.TabInfo
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class HomeViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    private val _uiLoadData = MutableLiveData<TabInfo>()

    val uiLoadData: LiveData<TabInfo>
        get() = _uiLoadData

    fun getCategoryOrder() {
        launch {
            repository.getHomeTabList()
                    .with(scheduler)
                    .subscribe(
                            { tabInfo ->
                                tabInfo?.let { _uiLoadData.postValue(tabInfo) }
                            }, {}
                    )
        }
    }
}