package com.mor.eye.view.base

import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.data.Photo

interface Callbacks {
    fun onCategoryClickListener(id: Int, iconUrl: String)
    fun onAuthorClickListener(id: Int, userType: String)
    fun onVideoClickListener(videoId: Int, feedUrl: String, playUrl: String, blurUrl: String, videoTitle: String)
    fun onDynamicInfoClickListener(item: ItemListBean)
    fun onBannerClickListener(url: String)
    fun onToggleListener(item: ItemListBean)
    fun onPictureClickListener(index: Int, urls: List<*>?, photo: Photo)
    fun onTagClickListener(id: Int)
    fun onAutoPlayVideoClickListener(item: ItemListBean)
}