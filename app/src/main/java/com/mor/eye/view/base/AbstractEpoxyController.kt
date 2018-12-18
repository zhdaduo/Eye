package com.mor.eye.view.base

import android.content.Context
import android.text.style.ImageSpan
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.StringUtils
import com.mor.eye.util.ktx.*
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.view.epoxy.AutoVideoBindingModel_
import com.mor.eye.view.epoxy.FooterTextCard_
import com.mor.eye.view.epoxy.HeaderTextCard_
import com.mor.eye.view.epoxy.PictureFollowBindingModel_

abstract class AbstractEpoxyController : EpoxyController() {

    fun buildBriefCard(callbacks: Callbacks, item: ItemListBean, id: Long, showCategory: Boolean): BriefCardBindingModel_ {
        return BriefCardBindingModel_()
                .id(id)
                .pictureUrl(item.data.icon)
                .titleText(item.data.title)
                .descriptionText(item.data.description)
                .showCategoryIfTrue(showCategory)
                .showFocusIfTrue(!showCategory)
                .categoryClick { _ -> callbacks.onCategoryClickListener(item.data.id, item.data.icon) }
    }

    fun buildBannerCard(callbacks: Callbacks, item: ItemListBean, id: Long, isCarousel: Boolean): BannerCardBindingModel_ {
        return BannerCardBindingModel_()
                .id(id)
                .isCarousel(isCarousel)
                .showAdIfTrue(item.data.label?.text.isNullOrBlank())
                .pictureUrl(item.data.image)
                .bannerClick { _ -> callbacks.onBannerClickListener(item.data.actionUrl!!) }
    }

    fun buildFollowCard(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long, isCarousel: Boolean, defaultUrl: String? = null): FollowCardBindingModel_ {
        val time = item.data.content?.data?.duration ?: item.data.duration
        val name = item.data.content?.data?.author?.name ?: item.data.author?.name
        val title = item.data.header?.title ?: item.data.title
        val category = item.data.content?.data?.category ?: item.data.category
        val feedUrl = item.data.content?.data?.cover?.feed ?: item.data.cover?.feed
        val coverUrl = item.data.header?.icon ?: item.data.author?.icon
        val playUrl = item.data.content?.data?.playUrl ?: item.data.playUrl
        val blurUrl = item.data.content?.data?.cover?.blurred ?: item.data.cover?.blurred
        val videoId = item.data.content?.data?.id ?: item.data.id
        val videoTitle = item.data.content?.data?.title ?: item.data.title
        val authorId = item.data.content?.data?.author?.id ?: item.data.author?.id

        return FollowCardBindingModel_()
                .id(id)
                .isCarousel(isCarousel)
                .timeText(StringUtils.durationFormat(time))
                .titleText(title)
                .descriptionText(String.format(context.getString(R.string.follow_description), name, category))
                .feedUrl(feedUrl)
                .coverUrl(coverUrl ?: defaultUrl)
                .showAdIfTrue(item.type == ViewTypeConstant.BANNER3)
                .showTimeIfTrue(true)
                .showSelectIfTrue(true)
                .videoClick { _ -> callbacks.onVideoClickListener(videoId, feedUrl!!, playUrl, blurUrl!!, videoTitle) }
                .authorClick { _ -> callbacks.onAuthorClickListener(authorId!!, "PGC") }
    }

    fun buildSmallCard(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long, isTextDefaultColor: Boolean = true): SmallCardBindingModel_ {
        return SmallCardBindingModel_()
                .id(id)
                .isTextDefaultColor(isTextDefaultColor)
                .timeText(StringUtils.durationFormat(item.data.duration))
                .titleText(item.data.title)
                .descriptionText(String.format(context.getString(R.string.small_description), item.data.category, item.data.author?.name))
                .feedUrl(item.data.cover?.feed)
                .videoClick { _ -> callbacks.onVideoClickListener(item.data.id, item.data.cover?.feed!!, item.data.playUrl, item.data.cover.blurred, item.data.title) }
    }

    fun buildVideoBriefCard(callbacks: Callbacks, item: ItemListBean, id: Long): VideoBriefCardBindingModel_ {
        return VideoBriefCardBindingModel_()
                .id(id)
                .titleText(item.data.header?.title)
                .descriptionText(item.data.header?.description)
                .coverUrl(item.data.header?.icon)
                .authorClick { _ -> callbacks.onAuthorClickListener(item.data.header?.id!!, "PGC") }
    }

    fun buildVideoBriefCarousel(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long): VideoBriefCarouselBindingModel_ {
        return VideoBriefCarouselBindingModel_()
                .id(id)
                .timeText(StringUtils.durationFormat(item.data.duration))
                .titleText(item.data.title)
                .descriptionText(String.format(context.getString(R.string.category1), item.data.category))
                .feedUrl(item.data.cover?.feed)
                .videoClick { _ -> callbacks.onVideoClickListener(item.data.id, item.data.cover?.feed!!, item.data.playUrl, item.data.cover.blurred, item.data.title) }

    }

    fun buildDynamicInfoCard(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long): DynamicInfoCardBindingModel_ {
        return DynamicInfoCardBindingModel_()
                .id(id)
                .avatarUrl(item.data.user.avatar)
                .nickNameText(item.data.user.nickname)
                .titleText(item.data.simpleVideo?.title)
                .subTitleText(item.data.text)
                .timeText(StringUtils.convertLongToTime(item.data.createDate))
                .messageText(item.data.reply?.message)
                .feedUrl(item.data.simpleVideo?.cover?.feed)
                .likeNumText(String.format(context.getString(R.string.like_num), item.data.reply?.likeCount ?: 0))
                .descriptionText(String.format(context.getString(R.string.category1), item.data.simpleVideo?.category))
                .authorClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, item.data.user.userType) }
                .videoClick { _ -> callbacks.onVideoClickListener(item.data.simpleVideo?.id!!, item.data.simpleVideo.cover.feed, item.data.simpleVideo.playUrl, item.data.simpleVideo.cover.blurred, item.data.simpleVideo.title) }
                .dynamicInfoClick { _ -> callbacks.onDynamicInfoClickListener(item) }
    }

    fun buildDynamicFollowCard(callbacks: Callbacks, item: ItemListBean, id: Long): DynamicFollowCardBindingModel_ {
        return DynamicFollowCardBindingModel_()
                .id(id)
                .avatarUrl(item.data.user.avatar)
                .nickNameText(item.data.user.nickname)
                .titleText(item.data.text)
                .timeText(StringUtils.convertLongToTime(item.data.createDate))
                .briefCardIconUrl(item.data.briefCard?.icon)
                .briefCardTitleText(item.data.briefCard?.title)
                .briefCardDesText(item.data.briefCard?.description)
                .showFocusIfTrue(item.data.briefCard?.follow?.isFollowed ?: false)
                .authorClick { _ -> callbacks.onAuthorClickListener(item.data.user.uid, item.data.user.userType) }
                .briefCardClick { _ -> callbacks.onAuthorClickListener(item.data.briefCard?.id!!, "PGC") }

    }

    fun buildPictureFollow(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long, defaultSubtitle: Boolean = true): PictureFollowBindingModel_ {
        val ownerBean = item.data.content?.data?.owner
        val consumptionBean = item.data.content?.data?.consumption
        val dataBean = item.data.content?.data
        val subTitle = if (defaultSubtitle) formatSubTitle(context, item) else formatSubTitle(context, dataBean?.title!!)
        return PictureFollowBindingModel_(context)
                .id(id)
                .item(item)
                .coverUrl(ownerBean?.avatar)
                .titleText(ownerBean?.nickname)
                .subTitleText(subTitle)
                .descriptionText(dataBean?.description)
                .likeNumText(consumptionBean?.collectionCount.toString())
                .commitNumText(consumptionBean?.replyCount.toString())
                .dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                .callback(callbacks)
                .imgGroupList(dataBean?.urls)
                .tagGroupList(dataBean?.tags?.map { it.name })
                .tagListener { str ->
                    val tag = dataBean?.tags?.first { it.name == str }
                    callbacks.onTagClickListener(tag?.id!!)
                }
                .toggleClick { _ -> callbacks.onToggleListener(item) }
                .authorClick { _ -> callbacks.onAuthorClickListener(ownerBean?.uid!!, ownerBean.userType) }
    }

    fun buildAutoVideo(callbacks: Callbacks, context: Context, item: ItemListBean, id: Long, defaultSubtitle: Boolean = true): AutoVideoBindingModel_ {
        val ownerBean = item.data.content?.data?.owner
        val authorBean = item.data.content?.data?.author
        val consumptionBean = item.data.content?.data?.consumption
        val dataBean = item.data.content?.data

        val avatar = ownerBean?.avatar ?: authorBean?.icon
        val nickname = ownerBean?.nickname ?: authorBean?.name
        val userId = ownerBean?.uid ?: authorBean?.id
        val userType = ownerBean?.userType ?: "PGC"
        val subTitle = if (defaultSubtitle) formatSubTitle(context, item) else formatSubTitle(context, dataBean?.title!!)
        return AutoVideoBindingModel_(context)
                .id(id)
                .pictureUrl(item.data.content?.data?.cover?.feed)
                .videoUrl(item.data.content?.data?.playUrl)
                .timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                .coverUrl(avatar)
                .titleText(nickname)
                .subTitleText(subTitle)
                .descriptionText(dataBean?.description)
                .likeNumText(consumptionBean?.collectionCount.toString())
                .commitNumText(consumptionBean?.replyCount.toString())
                .dateText(StringUtils.convertLongToTime(item.data.header?.time!!))
                .tagGroupList(dataBean?.tags?.map { it.name })
                .tagListener { str ->
                    val tag = dataBean?.tags?.first { it.name == str }
                    callbacks.onTagClickListener(tag?.id!!)
                }
                .authorClick { _ -> callbacks.onAuthorClickListener(userId!!, userType) }
                .autoPlayClick { _ -> callbacks.onAutoPlayVideoClickListener(item) }
                .toggleClick { _ -> callbacks.onToggleListener(item) }
    }

    fun buildHeaderTextCard(callbacks: Callbacks, context: Context, id: Long,
                            headerText: CharSequence? = null,
                            showArrow: Boolean = false,
                            headerDefaultColor: Boolean = true): HeaderTextCard_ {
        return HeaderTextCard_(context)
                .id(id)
                .headerDefaultColor(headerDefaultColor)
                .headerText(headerText)
                .showArrow(showArrow)
                .onClick { _ -> }
    }

    fun buildFooterTextCard(callbacks: Callbacks, context: Context, id: Long,
                            footerText: CharSequence? = null,
                            showArrow: Boolean = false): FooterTextCard_ {
        return FooterTextCard_(context)
                .id(id)
                .footerText(footerText)
                .showArrow(showArrow)
                .onClick { _ -> }
    }

    fun buildStatusEmpty(id: Long): StatusEmptyBindingModel_ {
        return StatusEmptyBindingModel_()
                .id(id)
    }

    fun buildStatusLoading(id: Long): StatusLoadingBindingModel_ {
        return StatusLoadingBindingModel_()
                .id(id)
    }

    fun buildStatusNoMore(id: Long): StatusNoMoreBindingModel_ {
        return StatusNoMoreBindingModel_()
                .id(id)
    }

    fun buildStatusNetInvalid(callbacks: Callbacks, id: Long): StatusNetInvalidBindingModel_ {
        return StatusNetInvalidBindingModel_()
                .id(id)
                .onClick { _ -> }
    }

    fun isHorizontalScrollCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.HORIZONTAL_CARD
    }

    fun isTextCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.TEXT_CARD
    }

    fun isHeader5(item: ItemListBean): Boolean {
        return item.data.type == ViewTypeConstant.HEADER5
    }

    fun isFooter2(item: ItemListBean): Boolean {
        return item.data.type == ViewTypeConstant.FOOTER2
    }

    fun isFollowCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.FOLLOW_CARD
    }

    fun isPictureFollowCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.PICTURE_FOLLOW_CARD
    }

    fun isAutoPlayFollowCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.AUTO_PLAY_FOLLOW_CARD
    }

    fun isSquareCardCollection(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.SQUARE_CARD_COLLECTION
    }

    fun isBriefCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.BRIEF_CARD
    }

    fun isVideoSmallCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.VIDEO_SMALL_CARD
    }

    fun isVideoCollectionWithBrief(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.VIDEO_WITH_BRIEF
    }

    fun isDynamicInfoCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.DYNAMIC_INFO_CARD
    }

    fun isVideo(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.VIDEO_CARD
    }

    fun isBanner2(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.BANNER2
    }

    fun isBanner(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.BANNER
    }

    fun isVideoCollectionOfHorizontalScrollCard(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.VIDEO_HORIZONTAL_CARD
    }

    fun isTextHeader(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.TEXT_HEADER
    }

    fun isTextFooter(item: ItemListBean): Boolean {
        return item.type == ViewTypeConstant.TEXT_FOOTER
    }

    private fun formatSubTitle(context: Context, title: String): CharSequence {
        return buildSpannedString {
            append("发布:")
            append(" ")
            bold {
                color(DisplayUtils.getColor(context, R.color.main_text_color)) {
                    append(title)
                }
            }
        }
    }

    private fun formatSubTitle(context: Context, item: ItemListBean): CharSequence {
        return if (!item.data.content?.data?.area.toString().isNullOrBlank()) {
            buildSpannedString {
                append("在  ")

                val drawable = DisplayUtils.getDrawable(context, R.drawable.ic_action_location_marker)
                icon(drawable, drawable.intrinsicWidth){
                    append(" ")
                }

                color(DisplayUtils.getColor(context, R.color.main_text_color)) {
                    val area = item.data.content?.data?.area.toString()
                    if (area.length > 10) {
                        append(area.substring(0, 10))
                        append("...")
                    } else append(area)
                }

                append(" 发布:")
            }
        } else "发布:"
    }
}
