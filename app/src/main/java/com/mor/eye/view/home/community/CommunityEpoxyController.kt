package com.mor.eye.view.home.community

import android.content.Context
import android.text.style.ImageSpan
import com.airbnb.epoxy.Carousel
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.StringUtils
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.ktx.CenterAlignImageSpan
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.Callbacks

class CommunityEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i: Long = 0

        if (isEmpty) {
            emptyState {
                id("empty_view")
            }
        } else {
            loadData.forEach { item ->
                val ownerBean = item.data.content?.data?.owner
                val consumptionBean = item.data.content?.data?.consumption

                if (item.type == ViewTypeConstant.SQUARE_CARD_COLLECTION) {
                    textHeader {
                        id(i++)
                        headerString(item.data.header?.title)
                        showArrow(!item.data.header?.actionUrl.isNullOrBlank())
                    }
                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        withModelsFrom(item.data.itemList!!) {
                            BannerCarouselBindingModel_()
                                    .id(it.data.id)
                                    .adText(it.data.label?.text)
                                    .pictureUrl(it.data.image)
                                    .bannerClick { _ -> callbacks.onBannerClickListener(it.data.actionUrl!!) }
                        }
                    }
                }

                if (item.type == ViewTypeConstant.PICTURE_FOLLOW_CARD) {
                    pictureFollow(ctx) {
                        id(i++)
                        item(item)
                        coverUrl(ownerBean?.avatar)
                        titleText(ownerBean?.nickname)
                        subTitleText(formatSubTitle(item))
                        descriptionText(item.data.content?.data?.description)
                        likeNumText(alignLikeNumTextWithImage(consumptionBean?.collectionCount.toString()))
                        commitNumText(alignCommitNumTextWithImage(consumptionBean?.replyCount.toString()))
                        dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                        callback(callbacks)
                        imgGroupList(item.data.content?.data?.urls)
                        tagGroupList(item.data.content?.data?.tags?.map { it.name })
                        tagListener { str ->
                            val tag = item.data.content?.data?.tags?.first { it.name == str }
                            callbacks.onTagClickListener(tag?.id!!)
                        }
                        toggleClick { _ -> callbacks.onToggleListener(item) }
                        authorClick { _ -> callbacks.onAuthorClickListener(ownerBean?.uid!!, ownerBean.userType) }
                    }
                }

                if (item.type == ViewTypeConstant.AUTO_PLAY_FOLLOW_CARD) {
                    autoVideo(ctx) {
                        id(i++)
                        pictureUrl(item.data.content?.data?.cover?.feed)
                        videoUrl(item.data.content?.data?.playUrl)
                        timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                        coverUrl(ownerBean?.avatar)
                        titleText(ownerBean?.nickname)
                        subTitleText(formatSubTitle(item))
                        descriptionText(item.data.content.data.description)
                        likeNumText(alignLikeNumTextWithImage(consumptionBean?.collectionCount.toString()))
                        commitNumText(alignCommitNumTextWithImage(consumptionBean?.replyCount.toString()))
                        dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                        tagGroupList(item.data.content.data.tags?.map { it.name })
                        tagListener { str ->
                            val tag = item.data.content.data.tags?.first { it.name == str }
                            callbacks.onTagClickListener(tag?.id!!)
                        }
                        authorClick { _ -> callbacks.onAuthorClickListener(ownerBean?.uid!!, ownerBean.userType) }
                        autoPlayClick { _ -> callbacks.onAutoPlayVideoClickListener(item) }
                        toggleClick { _ -> callbacks.onToggleListener(item) }
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

    fun addAll(loadMoreItems: List<ItemListBean>) {
        loadData.addAll(loadMoreItems)
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

    private fun alignLikeNumTextWithImage(num: String): CharSequence {
        val drawable = DisplayUtils.getDrawable(ctx, R.drawable.ic_action_heart)
        return buildSpannedString {
            append(" ")
            append(" ")
            append(num)
            drawable.let {
                it.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                setSpan(CenterAlignImageSpan(drawable), 0, 1, ImageSpan.ALIGN_BASELINE)
            }
        }
    }

    private fun alignCommitNumTextWithImage(num: String): CharSequence {
        val drawable = DisplayUtils.getDrawable(ctx, R.drawable.ic_action_message)
        return buildSpannedString {
            append(" ")
            append(" ")
            append(num)
            drawable.let {
                it.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                setSpan(CenterAlignImageSpan(drawable), 0, 1, ImageSpan.ALIGN_BASELINE)
            }
        }
    }

    private fun formatSubTitle(item: ItemListBean): CharSequence {
        return if (!item.data.content?.data?.area.toString().isNullOrBlank()) {
            buildSpannedString {
                append("在  ")
                color(DisplayUtils.getColor(ctx, R.color.main_text_color)) {
                    val area = item.data.content?.data?.area.toString()
                    if (area.length > 10) {
                        append(area.substring(0, 10))
                        append("...")
                    } else append(area)
                }
                append(" 发布:")
                val drawable = DisplayUtils.getDrawable(ctx, R.drawable.ic_action_location_marker)
                drawable.let {
                    it.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                    setSpan(CenterAlignImageSpan(drawable), 2, 3, ImageSpan.ALIGN_BASELINE)
                }
            }
        } else "发布:"
    }
}
