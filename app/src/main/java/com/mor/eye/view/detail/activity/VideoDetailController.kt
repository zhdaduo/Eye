package com.mor.eye.view.detail.activity

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.Relate
import com.mor.eye.repository.data.Video
import com.mor.eye.util.StringUtils
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.other.isNetworkAvailable
import com.mor.eye.view.base.Callbacks

class VideoDetailController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isNoMore = false

    private var videoBean: Video? = null
    private var loadItems = mutableListOf<Relate.ItemList>()

    override fun buildModels() {
        var i = 0L

        if (!ctx.isNetworkAvailable) {
            netInvalidState {
                id("net_invalid")
            }
        } else {
            videoDetailContent {
                id(i++)
                titleText(videoBean?.title)
                categoryText(String.format(ctx.getString(R.string.category1), videoBean?.category))
                descriptionText(videoBean?.description)
                likeNumText(videoBean?.consumption?.collectionCount.toString())
                shareNumText(videoBean?.consumption?.shareCount.toString())
                replyNumText(videoBean?.consumption?.replyCount.toString())
                likeClick { _ -> {} }
                shareClick { _ -> {} }
                replyClick { _ -> {} }
                downloadClick { _ -> {} }
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
                                .tagClick { _ -> {} }
                    }
                }
            }

            videoBean?.author?.let {
                videoDetailHeader {
                    id(i++)
                    authorUrl(it.icon)
                    authorNameText(it.name)
                    authorDesText(it.description)
                    authorClick { _ -> {} }
                    focusClick { _ -> {} }
                }
            }

            textHeaderWhite {
                id(i++)
                headerString("相关推荐")
                showArrow(false)
            }

            loadItems.map { item ->

                if (item.type == "videoSmallCard") {
                    smallCardWhite {
                        id(i++)
                        timeText(StringUtils.durationFormat(item.data.duration!!))
                        titleText(item.data.title)
                        descriptionText(String.format(ctx.getString(R.string.small_description), item.data.category, item.data.author?.name))
                        feedUrl(item.data.cover?.feed)
                        videoClick { _ -> {} }
                    }
                }
            }

            if (isNoMore) {
                textEnd {
                    id("no_more_view")
                }
            }
        }
    }

    fun update(video: Video) {
        videoBean = video
        requestModelBuild()
    }

    fun updateCom(itemBean: List<Relate.ItemList>) {
        loadItems.addAll(itemBean)
        requestModelBuild()
    }

    fun showEnd() {
        isNoMore = true
        requestModelBuild()
    }
}