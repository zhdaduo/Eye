package com.mor.eye.view.detail.activity

import android.content.Context
import com.mor.eye.R
import com.mor.eye.videoDetailContent
import com.mor.eye.VideoDetailTagBindingModel_
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.videoDetailHeader
import com.mor.eye.repository.data.Video
import com.mor.eye.util.NetworkUtils
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class VideoDetailController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isNoMore = false

    private var videoBean: Video? = null
    private var loadItems = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i = 0L

        if (!NetworkUtils.isNetworkConnected(ctx)) {
            buildStatusNetInvalid(callbacks, i++)
        } else {
            videoDetailContent {
                id(i++)
                titleText(videoBean?.title)
                categoryText(String.format(ctx.getString(R.string.category1), videoBean?.category))
                descriptionText(videoBean?.description)
                likeNumText(videoBean?.consumption?.collectionCount.toString())
                shareNumText(videoBean?.consumption?.shareCount.toString())
                replyNumText(videoBean?.consumption?.replyCount.toString())
                likeClick { _ -> videoCallback?.likeCallback() }
                shareClick { _ -> videoCallback?.shareCallback() }
                replyClick { _ -> videoCallback?.replyCallback() }
                downloadClick { _ -> videoCallback?.downloadCallback() }
            }

            videoBean?.tags?.let {
                carousel {
                    id(i++)
                    hasFixedSize(true)
                    withModelsFrom(it) { tagsBean ->
                        VideoDetailTagBindingModel_()
                                .id(i++)
                                .tagImageUrl(tagsBean.bgPicture)
                                .tagTitleText(String.format(ctx.getString(R.string.tags), tagsBean.name))
                                .tagClick { _ -> callbacks.onTagClickListener(tagsBean.id.toInt()) }
                    }
                }
            }

            videoBean?.author?.let {
                videoDetailHeader {
                    id(i++)
                    authorUrl(it.icon)
                    authorNameText(it.name)
                    authorDesText(it.description)
                    authorClick { _ -> callbacks.onAuthorClickListener(it.id.toInt(), "PGC") }
                    focusClick { _ -> videoCallback?.focusCallback() }
                }
            }

            buildHeaderTextCard(callbacks, ctx, i++, "相关推荐", headerDefaultColor = false)
                    .addTo(this)

            loadItems.map { item ->

                buildSmallCard(callbacks, ctx, item, i++, false)
                        .addIf(item.type == "videoSmallCard", this)
            }

            buildStatusNoMore(i++).addIf(isNoMore, this)
        }
    }

    fun update(video: Video) {
        videoBean = video
        requestModelBuild()
    }

    fun updateCom(itemBean: List<ItemListBean>) {
        loadItems.addAll(itemBean)
        requestModelBuild()
    }

    fun showEnd() {
        isNoMore = true
        requestModelBuild()
    }

    private var videoCallback: OnVideoCallback? = null

    fun setVideoCallback(videoCallback: OnVideoCallback) {
        this.videoCallback = videoCallback
    }

    interface OnVideoCallback {
        fun likeCallback()
        fun shareCallback()
        fun replyCallback()
        fun downloadCallback()
        fun focusCallback()
    }
}