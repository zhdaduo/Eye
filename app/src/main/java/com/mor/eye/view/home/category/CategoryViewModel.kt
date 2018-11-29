package com.mor.eye.view.home.category

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class CategoryViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    private val _uiLoadData = MutableLiveData<List<ItemListBean>>()

    val uiLoadData: LiveData<List<ItemListBean>>
        get() = _uiLoadData

    fun getCategoryData() {
        launch {
            repository.getAllCategory()
                    .with(scheduler)
                    .subscribe(
                            { findBean ->
                                _uiLoadData.postValue(findBean.itemList)
                            }, {}
                    )
        }
    }
}