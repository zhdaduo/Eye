package com.mor.eye.view.home.recommend

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.StringUtils
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.ktx.bold
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.Callbacks

class RecommendEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private var loadData = mutableListOf<ItemListBean>()
    private var loadMoreData = mutableListOf<ItemListBean>()
    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    override fun buildModels() {
        var i = 0L

        val squareCardCollectionData = loadData.first { it.type == ViewTypeConstant.SQUARE_CARD_COLLECTION }
        val textCardData = loadMoreData.filter { it.type == ViewTypeConstant.TEXT_CARD }
        val followCardLoadMore = loadMoreData.filter { it.type == ViewTypeConstant.FOLLOW_CARD }

        if (isEmpty) {
            emptyState {
                id("empty_view")
            }
        } else {
            textHeader {
                val builder = buildSpannedString {
                    append(squareCardCollectionData.data.header?.subTitle + "\n")
                    bold {
                        color(DisplayUtils.getColor(ctx, R.color.tv_hint)) {
                            append(squareCardCollectionData.data.header?.title)
                        }
                    }
                }
                id("header1")
                headerString(builder.toString())
            }

            carousel {
                id(i++)
                padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                hasFixedSize(true)
                withModelsFrom(squareCardCollectionData.data.itemList!!) { item ->
                    FollowCarouselBindingModel_()
                            .id(i++)
                            .timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                            .titleText(item.data.header?.title)
                            .descriptionText(String.format(ctx.getString(R.string.follow_description), item.data.content.data.author?.name, item.data.content.data.category))
                            .feedUrl(item.data.content.data.cover?.feed)
                            .coverUrl(item.data.header?.icon)
                            .showAdIfTrue(item.type == ViewTypeConstant.BANNER3)
                            .showTimeIfTrue(true)
                            .showSelectIfTrue(true)
                            .videoClick { _ -> callbacks.onVideoClickListener(item.data.content.data.id) }
                            .authorClick { _ -> callbacks.onAuthorClickListener(item.data.content.data.author?.id!!, "PGC") }
                }
            }

            loadData.forEach { item ->
                if (item.type == ViewTypeConstant.TEXT_CARD && item.data.text != "社区精选" && item.data.text != "查看全部热门排行") {
                    textHeader {
                        id(i++)
                        headerString(item.data.text)
                        showArrow(!item.data.actionUrl.isNullOrBlank())
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
                if (item.type == ViewTypeConstant.BANNER2 || item.type == ViewTypeConstant.BANNER) {
                    bannerCardSingle {
                        id(i++)
                        pictureUrl(item.data.image)
                        bannerClick { _ -> callbacks.onBannerClickListener(item.data.actionUrl!!) }
                    }
                }
            }

            if (textCardData.isNotEmpty()) {
                textHeader {
                    id("header2")
                    headerString("猜你喜欢")
                }
            }

            followCardLoadMore.map { item ->
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
            loadMoreData.clear()
            loadData.addAll(loadItems)
            isLoadMore = false
            requestModelBuild()
        }
    }

    fun addLoadData(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
    }

    fun addLoadMoreData(loadMoreItems: List<ItemListBean>) {
        loadMoreData.addAll(loadMoreItems)
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