package com.mor.eye.view.detail.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.AuthorDetailBean
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class AuthorDetailViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    private val _uiLoadData = MutableLiveData<AuthorDetailBean>()

    var userType = "PGC"
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

    val uiLoadData: LiveData<AuthorDetailBean>
        get() = _uiLoadData

    fun refresh() {
        launch {
            repository.getAuthorTabDetailData(id, userType)
                    .with(scheduler)
                    .subscribe(
                            { bean ->
                                if (bean != null) {
                                    _uiLoadData.postValue(bean)
                                }
                            }, {}
                    )
        }
    }
}