package com.mor.eye.view.home.discovery

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.StringUtils
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.Callbacks

class FindEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private var loadData = mutableListOf<ItemListBean>()
    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    override fun buildModels() {
        var i: Long = 0
        if (isEmpty) {
            emptyState {
                id("empty_view")
            }
        } else {
            loadData.forEach { item ->
                if (item.type == ViewTypeConstant.HORIZONTAL_CARD) {
                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            BannerCarouselBindingModel_()
                                    .id(i++)
                                    .adText(it.data.label?.text)
                                    .pictureUrl(it.data.image)
                                    .bannerClick { _ -> callbacks.onBannerClickListener(it.data.actionUrl!!) }
                        }
                    }
                }
                if (item.type == ViewTypeConstant.TEXT_CARD) {
                    if (item.data.type == ViewTypeConstant.HEADER5) {
                        textHeader {
                            id(i++)
                            headerString(item.data.text)
                            showArrow(!item.data.actionUrl.isNullOrBlank())
                        }
                    }
                    if (item.data.type == ViewTypeConstant.FOOTER2) {
                        textFooter {
                            id(i++)
                            footerString(item.data.text)
                            showArrow(!item.data.actionUrl.isNullOrBlank())
                        }
                    }
                }
                if (item.type == ViewTypeConstant.FOLLOW_CARD) {
                    followCard {
                        id(i++)
                        timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                        titleText(item.data.header?.title)
                        descriptionText(String.format(ctx.getString(R.string.follow_description), item.data.content.data.author?.name, item.data.content.data.category))
                        feedUrl(item.data.content.data.cover?.feed)
                        coverUrl(item.data.header?.icon)
                        showAdIfTrue(item.type == ViewTypeConstant.BANNER3)
                        showTimeIfTrue(true)
                        showSelectIfTrue(true)
                        videoClick { _ -> callbacks.onVideoClickListener(item.data.content.data.id) }
                        authorClick { _ -> callbacks.onAuthorClickListener(item.data.content.data.author?.id!!, "PGC") }
                    }
                }
                if (item.type == ViewTypeConstant.VIDEO_SMALL_CARD) {
                    smallCard {
                        id(i++)
                        timeText(StringUtils.durationFormat(item.data.duration))
                        titleText(item.data.title)
                        descriptionText(String.format(ctx.getString(R.string.small_description), item.data.category, item.data.author?.name))
                        feedUrl(item.data.cover?.feed)
                        videoClick { _ -> callbacks.onVideoClickListener(item.data.id) }
                    }
                }
                if (item.type == ViewTypeConstant.BRIEF_CARD) {
                    briefCard {
                        id(i++)
                        pictureUrl(item.data.icon)
                        titleText(item.data.title)
                        descriptionText(item.data.description)
                        showCategoryIfTrue(false)
                        showFocusIfTrue(true)
                        categoryClick { _ -> callbacks.onCategoryClickListener(item.data.id, item.data.icon) }
                    }
                }
                if (item.type == ViewTypeConstant.SQUARE_CARD_COLLECTION) {
                    textHeader {
                        id(i++)
                        headerString(item.data.header?.title)
                        showArrow(true)
                    }
                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            BannerCarouselBindingModel_()
                                    .id(it.data.id)
                                    .adText(it.data.label?.text)
                                    .pictureUrl(it.data.image)
                                    .bannerClick { _ -> callbacks.onBannerClickListener(it.data.actionUrl!!) }
                        }
                    }
                }
                if (item.type == ViewTypeConstant.VIDEO_WITH_BRIEF) {
                    videoBrief {
                        id(i++)
                        titleText(item.data.header?.title)
                        descriptionText(item.data.header?.description)
                        coverUrl(item.data.header?.icon)
                        authorClick { _ -> callbacks.onAuthorClickListener(item.data.header?.id!!, "PGC") }
                    }

                    carousel {
                        id(i++)
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            VideoBriefCarouselBindingModel_()
                                    .id(i++)
                                    .timeText(StringUtils.durationFormat(it.data.duration))
                                    .titleText(it.data.title)
                                    .descriptionText(String.format(ctx.getString(R.string.category1), it.data.category))
                                    .feedUrl(it.data.cover?.feed)
                                    .videoClick { _ -> callbacks.onVideoClickListener(it.data.id) }
                        }
                    }
                }
                if (item.type == ViewTypeConstant.DYNAMIC_INFO_CARD) {
                    dynamicInfoCard {
                        id(i++)
                        avatarUrl(item.data.user.avatar)
                        nickNameText(item.data.user.nickname)
                        titleText(item.data.simpleVideo?.title)
                        subTitleText(item.data.text)
                        timeText(StringUtils.convertLongToTime(item.data.createDate))
                        messageText(item.data.reply?.message)
                        feedUrl(item.data.simpleVideo?.cover?.feed)
                        likeNumText(String.format(ctx.getString(R.string.like_num), item.data.reply?.likeCount.toString()))
                        descriptionText(String.format(ctx.getString(R.string.category1), item.data.simpleVideo?.category))
                        authorClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, item.data.user.userType) }
                        videoClick { _ -> callbacks.onVideoClickListener(item.data.simpleVideo?.id!!) }
                        dynamicInfoClick { _ -> callbacks.onDynamicInfoClickListener(item) }
                    }
                }
            }

            if (isLoadMore) {
                infiniteLoading {
                    id("loading_view")
                }
            }

            if (isNoMore) {
                textEnd {
                    id("no_more_view")
                }
            }
        }
    }

    fun update(loadItems: List<ItemListBean>) {
        if (loadData == loadItems) {
            return
        } else {
            loadData.clear()
            loadData.addAll(loadItems)
            isLoadMore = false
            requestModelBuild()
        }
    }

    fun addAll(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
    }

    fun showFooter() {
        isLoadMore = true
        requestModelBuild()
    }

    fun showEnd() {
        isNoMore = true
        isLoadMore = false
        requestModelBuild()
    }

    fun showEmpty() {
        isEmpty = true
        requestModelBuild()
    }
}