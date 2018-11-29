package com.mor.eye.view.base

import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.data.Photo

interface Callbacks {
    fun onCategoryClickListener(id: Int, iconUrl: String)
    fun onAuthorClickListener(id: Int, userType: String)
    fun onVideoClickListener(videoId: Int)
    fun onDynamicInfoClickListener(item: ItemListBean)
    fun onBannerClickListener(url: String)
    fun onToggleListener(item: ItemListBean)
    fun onPictureClickListener(index: Int, urls: ArrayList<String>, photo: Photo)
    fun onTagClickListener(id: Int)
    fun onAutoPlayVideoClickListener(item: ItemListBean)
}