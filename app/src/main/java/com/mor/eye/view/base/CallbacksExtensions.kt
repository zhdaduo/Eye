package com.mor.eye.view.base

import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.repository.data.Photo

typealias OnSomeActionListener = (item: ItemListBean) -> Unit

class CallbacksExtensions(
        private var categoryClickListener: ((id: Int, iconUrl: String) -> Unit) = { _, _ -> },
        private var authorClickListener: ((id: Int, userType: String) -> Unit) = { _, _ -> },
        private var videoClickListener: ((videoId: Int) -> Unit) = { _ -> },
        private var autoPlayVideoClickListener: OnSomeActionListener = { _ -> },
        private var dynamicInfoClickListener: OnSomeActionListener = { _ -> },
        private var bannerClickListener: ((url: String) -> Unit) = { _ -> },
        private var toggleListener: OnSomeActionListener = { _ -> },
        private var pictureListener: ((index: Int, urls: ArrayList<String>, photo: Photo) -> Unit) = { _, _, _ -> },
        private var tagClickListener: ((id: Int) -> Unit) = { _ -> }
) : Callbacks {
    override fun onAutoPlayVideoClickListener(item: ItemListBean) = autoPlayVideoClickListener(item)

    override fun onCategoryClickListener(id: Int, iconUrl: String) = categoryClickListener(id, iconUrl)

    override fun onAuthorClickListener(id: Int, userType: String) = authorClickListener(id, userType)

    override fun onVideoClickListener(videoId: Int) = videoClickListener(videoId)

    override fun onDynamicInfoClickListener(item: ItemListBean) = dynamicInfoClickListener(item)

    override fun onBannerClickListener(url: String) = bannerClickListener(url)

    override fun onToggleListener(item: ItemListBean) = toggleListener(item)

    override fun onTagClickListener(id: Int) = tagClickListener(id)

    override fun onPictureClickListener(index: Int, urls: ArrayList<String>, photo: Photo) = pictureListener(index, urls, photo)

    fun categoryClick(listener: (id: Int, iconUrl: String) -> Unit) = apply { categoryClickListener = listener }

    fun authorClick(listener: (id: Int, userType: String) -> Unit) = apply { authorClickListener = listener }

    fun videoClick(listener: (videoId: Int) -> Unit) = apply { videoClickListener = listener }

    fun autoPlayVideoClick(listener: (item: ItemListBean) -> Unit) = apply { autoPlayVideoClickListener = listener }

    fun dynamicInfoClick(listener: (item: ItemListBean) -> Unit) = apply { dynamicInfoClickListener = listener }

    fun bannerClick(listener: (url: String) -> Unit) = apply { bannerClickListener = listener }

    fun toggleClick(listener: (item: ItemListBean) -> Unit) = apply { toggleListener = listener }

    fun tagClickListener(listener: (id: Int) -> Unit) = apply { tagClickListener = listener }

    fun pictureClick(listener: (index: Int, urls: ArrayList<String>, photo: Photo) -> Unit) = apply { pictureListener = listener }
}