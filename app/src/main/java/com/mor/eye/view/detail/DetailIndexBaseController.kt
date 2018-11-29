package com.mor.eye.view.detail

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
import com.mor.eye.util.ktx.bold
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.Callbacks
import com.mor.eye.view.home.community.autoVideo
import com.mor.eye.view.home.community.pictureFollow

class DetailIndexBaseController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    private var defaultUrl: String? = null
    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i: Long = 0
        if (isEmpty) {
            emptyState {
                id("empty_view")
            }
        } else {
            loadData.forEach { item ->
                if (item.type == ViewTypeConstant.VIDEO_HORIZONTAL_CARD) {
                    textHeader {
                        id(i++)
                        headerString(item.data.header?.title)
                        showArrow(item.data.header?.actionUrl != null)
                    }

                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            FollowCarouselBindingModel_()
                                    .id(i++)
                                    .timeText(StringUtils.durationFormat(it.data.duration))
                                    .titleText(it.data.title)
                                    .descriptionText(String.format(ctx.getString(R.string.follow_description), it.data.author?.name, it.data.category))
                                    .feedUrl(it.data.cover?.feed)
                                    .coverUrl(it.data.author?.icon)
                                    .showAdIfTrue(false)
                                    .showTimeIfTrue(false)
                                    .showSelectIfTrue(false)
                                    .authorClick { _ -> callbacks.onAuthorClickListener(it.data.author?.id!!, "PGC") }
                                    .videoClick { _ -> callbacks.onVideoClickListener(it.data.id) }
                        }
                    }
                }

                if (item.type == ViewTypeConstant.TEXT_HEADER) {
                    if (item.data.text != "关注列表") {
                        textHeader {
                            id(i++)
                            headerString(item.data.text)
                            showArrow(false)
                        }
                    }
                }

                if (item.type == ViewTypeConstant.TEXT_FOOTER) {
                    if (item.data.text != "查看所有关注") {
                        textFooter {
                            id(i++)
                            footerString(item.data.text)
                            showArrow(!item.data.actionUrl.isNullOrBlank())
                        }
                    }
                }

                if (item.type == ViewTypeConstant.VIDEO_CARD) {
                    followCard {
                        id(i++)
                        timeText(StringUtils.durationFormat(item.data.duration))
                        titleText(item.data.title)
                        descriptionText(String.format(ctx.getString(R.string.follow_description), item.data.author?.name, item.data.category))
                        feedUrl(item.data.cover?.feed)
                        coverUrl(item.data.author?.icon ?: defaultUrl)
                        showAdIfTrue(false)
                        showTimeIfTrue(true)
                        showSelectIfTrue(false)
                        videoClick { _ -> callbacks.onVideoClickListener(item.data.id) }
                        authorClick { _ -> callbacks.onAuthorClickListener(item.data.author?.id!!, "PGC") }
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
                    val showFocus = item.data.briefCard?.follow?.isFollowed ?: false
                    val likeNum = item.data.reply?.likeCount?.let { String.format(ctx.getString(R.string.like_num), item.data.reply.likeCount.toString()) }
                            ?: String.format(ctx.getString(R.string.like_num), 0)
                    item.data.briefCard?.let {
                        dynamicFollowCard {
                            id(i++)
                            avatarUrl(item.data.user.avatar)
                            nickNameText(item.data.user.nickname)
                            titleText(item.data.text)
                            timeText(StringUtils.convertLongToTime(item.data.createDate))
                            briefCardIconUrl(item.data.briefCard.icon)
                            briefCardTitleText(item.data.briefCard.title)
                            briefCardDesText(item.data.briefCard.description)
                            showFocusIfTrue(showFocus)
                            authorClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, item.data.user.userType) }
                            briefCardClick { _ -> callbacks.onAuthorClickListener(item.data.briefCard.id, "PGC") }
                        }
                    }
                    item.data.simpleVideo?.let {
                        dynamicInfoCard {
                            id(i++)
                            avatarUrl(item.data.user.avatar)
                            nickNameText(item.data.user.nickname)
                            titleText(item.data.simpleVideo.title)
                            subTitleText(item.data.text)
                            timeText(StringUtils.convertLongToTime(item.data.createDate))
                            messageText(item.data.reply?.message)
                            feedUrl(item.data.simpleVideo.cover.feed)
                            likeNumText(likeNum)
                            descriptionText(String.format(ctx.getString(R.string.category1), item.data.simpleVideo.category))
                            authorClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, item.data.user.userType) }
                            videoClick { _ -> callbacks.onVideoClickListener(item.data.simpleVideo.id) }
                            dynamicInfoClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, "PGC") }
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

                if (item.type == ViewTypeConstant.PICTURE_FOLLOW_CARD) {
                    val ownerBean = item.data.content?.data?.owner
                    val consumptionBean = item.data.content?.data?.consumption
                    val dataBean = item.data.content?.data
                    pictureFollow(ctx) {
                        id(i++)
                        item(item)
                        coverUrl(ownerBean?.avatar)
                        titleText(ownerBean?.nickname)
                        subTitleText(formatSubTitle(dataBean?.title!!))
                        descriptionText(dataBean.description)
                        likeNumText(alignLikeNumTextWithImage(consumptionBean?.collectionCount.toString()))
                        commitNumText(alignCommitNumTextWithImage(consumptionBean?.replyCount.toString()))
                        dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                        callback(callbacks)
                        imgGroupList(dataBean.urls)
                        tagGroupList(dataBean.tags?.map { it.name })
                        tagListener { str ->
                            val tag = dataBean.tags?.first { it.name == str }
                            callbacks.onTagClickListener(tag?.id!!)
                        }
                        toggleClick { _ -> callbacks.onToggleListener(item) }
                        authorClick { _ -> callbacks.onAuthorClickListener(ownerBean?.uid!!, ownerBean.userType) }
                    }
                }

                if (item.type == ViewTypeConstant.AUTO_PLAY_FOLLOW_CARD) {
                    val ownerBean = item.data.content?.data?.owner
                    val authorBean = item.data.content?.data?.author
                    val consumptionBean = item.data.content?.data?.consumption
                    val dataBean = item.data.content?.data

                    val avatar = ownerBean?.avatar ?: authorBean?.icon
                    val nickname = ownerBean?.nickname ?: authorBean?.name
                    val id = ownerBean?.uid ?: authorBean?.id
                    val userType = ownerBean?.userType ?: "PGC"

                    autoVideo(ctx) {
                        id(i++)
                        pictureUrl(item.data.content?.data?.cover?.feed)
                        videoUrl(item.data.content?.data?.playUrl)
                        timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                        coverUrl(avatar)
                        titleText(nickname)
                        subTitleText(formatSubTitle(dataBean?.title!!))
                        descriptionText(dataBean.description)
                        likeNumText(alignLikeNumTextWithImage(consumptionBean?.collectionCount.toString()))
                        commitNumText(alignCommitNumTextWithImage(consumptionBean?.replyCount.toString()))
                        dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                        tagGroupList(dataBean.tags?.map { it.name })
                        tagListener { str ->
                            val tag = dataBean.tags?.first { it.name == str }
                            callbacks.onTagClickListener(tag?.id!!)
                        }
                        authorClick { _ -> callbacks.onAuthorClickListener(id!!, userType) }
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

    fun addAll(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
    }

    fun setDefaultUrl(defaultUrl: String) {
        this.defaultUrl = defaultUrl
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
            append("\t")
            append("\t")
            append(num)
            drawable.let {
                it.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
                setSpan(CenterAlignImageSpan(drawable), 0, 1, ImageSpan.ALIGN_BASELINE)
            }
        }
    }

    private fun formatSubTitle(title: String): CharSequence {
        return buildSpannedString {
            append("发布:")
            append(" ")
            bold {
                color(DisplayUtils.getColor(ctx, R.color.main_text_color)) {
                    append(title)
                }
            }
        }
    }
}