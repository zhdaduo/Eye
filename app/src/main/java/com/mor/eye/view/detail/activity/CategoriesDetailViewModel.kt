package com.mor.eye.view.detail.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.CategoryDetailBean
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class CategoriesDetailViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    private val _uiLoadData = MutableLiveData<CategoryDetailBean>()

    var id = "0"
        set(value) {
            if (value != field) {
                field = value
            }
        }

    val uiLoadData: LiveData<CategoryDetailBean>
        get() = _uiLoadData

    fun refresh() {
        launch {
            repository.getCategoryTabDetailData(id)
                    .with(scheduler)
                    .subscribe(
                            { bean ->
                                if (bean != null) { _uiLoadData.postValue(bean) }
                            }, {}
                    )
        }
    }
}