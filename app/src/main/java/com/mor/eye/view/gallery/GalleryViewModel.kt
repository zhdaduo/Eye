package com.mor.eye.view.gallery

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {
    internal val darkMode = MutableLiveData<Boolean>()
    var currentPosition: Int = 0

    init {
        darkMode.value = false
    }
}