package com.mor.eye.view.detail

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class DetailIndexBaseController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    private var defaultUrl: String? = null
    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i = 0L

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {
            loadData.forEach { item ->
                if (isVideoCollectionOfHorizontalScrollCard(item)) {
                    buildHeaderTextCard(callbacks, ctx, i++, item.data.header?.title, !item.data.header?.actionUrl.isNullOrBlank())
                            .addTo(this)

                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            buildFollowCard(callbacks, ctx, it, i++, true)
                        }
                    }
                }

                if (isTextHeader(item)) {
                    if (item.data.text != "关注列表") {
                        buildHeaderTextCard(callbacks, ctx, i++, item.data.text)
                                .addTo(this)
                    }
                }

                if (isTextFooter(item)) {
                    if (item.data.text != "查看所有关注") {
                        buildFooterTextCard(callbacks, ctx, i++, item.data.text, !item.data.actionUrl.isNullOrBlank())
                                .addTo(this)
                    }
                }

                if (isVideo(item)) {
                    buildFollowCard(callbacks, ctx, item, i++, false, defaultUrl)
                            .addTo(this)
                }

                if (isVideoCollectionWithBrief(item)) {
                    buildVideoBriefCard(callbacks, item, i++)
                            .addTo(this)

                    carousel {
                        id(i++)
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            buildVideoBriefCarousel(callbacks, ctx, it, i++)
                        }
                    }
                }

                if (isDynamicInfoCard(item)) {

                    item.data.briefCard?.let {
                        buildDynamicFollowCard(callbacks, item, i++)
                                .addTo(this)
                    }
                    item.data.simpleVideo?.let {
                        buildDynamicInfoCard(callbacks, ctx, item, i++)
                                .addTo(this)
                    }
                }

                if (isFollowCard(item)) {
                    buildFollowCard(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }

                if (isPictureFollowCard(item)) {
                    buildPictureFollow(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }

                if (isAutoPlayFollowCard(item)) {
                    buildAutoVideo(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }
            }

            buildStatusLoading(i++).addIf(isLoadMore, this)

            buildStatusNoMore(i++).addIf(isNoMore, this)
        }
    }

    fun update(loadItems: List<ItemListBean>) {
        loadData.clear()
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
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
}