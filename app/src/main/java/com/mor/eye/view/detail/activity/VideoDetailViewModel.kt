package com.mor.eye.view.detail.activity

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.mor.eye.repository.EyeRepository
import com.mor.eye.repository.data.Relate
import com.mor.eye.repository.data.Video
import com.mor.eye.util.rx.SchedulerProvider
import com.mor.eye.util.rx.with
import com.mor.eye.view.base.RxViewModel

class VideoDetailViewModel(private val repository: EyeRepository, private val scheduler: SchedulerProvider) : RxViewModel() {
    private val _uiLoadData = MutableLiveData<List<Relate.ItemList>>()
    private val _uiVideoData = MutableLiveData<Video>()

    var id = "0"
        set(value) {
            if (value != field) {
                field = value
            }
        }

    val uiLoadData: LiveData<List<Relate.ItemList>>
        get() = _uiLoadData
    val uiVideoData: LiveData<Video>
        get() = _uiVideoData

    fun getRecommend() {
        launch {
            repository.getDetailRecommendData(id)
                    .with(scheduler)
                    .subscribe(
                            { bean ->
                                if (bean != null) {
                                    _uiLoadData.postValue(bean.itemList)
                                }
                            }, {}
                    )
        }
    }

    fun getVideoDetail() {
        launch {
            repository.getVideoDetailData(id)
                    .with(scheduler)
                    .subscribe(
                            { video ->
                                if (video != null) {
                                    _uiVideoData.postValue(video)
                                }
                            }, {}
                    )
        }
    }
}