package com.mor.eye.view.home.discovery

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class FindEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private var loadData = mutableListOf<ItemListBean>()
    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    override fun buildModels() {
        var i = 0L

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {
            loadData.forEach { item ->
                if (isHorizontalScrollCard(item)) {
                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            buildBannerCard(callbacks, it, i++, true)
                        }
                    }
                }
                if (isTextCard(item) && item.data.header?.title != "近期专题") {
                    if (isHeader5(item)) {
                        buildHeaderTextCard(callbacks, ctx, i++, item.data.text, !item.data.actionUrl.isNullOrBlank())
                                .addTo(this)
                    }
                    if (isFooter2(item)) {
                        buildFooterTextCard(callbacks, ctx, i++, item.data.text, !item.data.actionUrl.isNullOrBlank())
                                .addTo(this)
                    }
                }

                if (isFollowCard(item)) {
                    buildFollowCard(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }
                if (isVideoSmallCard(item)) {
                    buildSmallCard(callbacks, ctx, item, i++)
                            .addTo(this)
                }
                if (isBriefCard(item)) {
                    buildBriefCard(callbacks, item, i++, false)
                            .addTo(this)
                }
                if (isSquareCardCollection(item)) {
                    if (item.data.header?.title == "近期专题") {
                        buildHeaderTextCard(callbacks, ctx, i++, item.data.header.title, true)
                                .addTo(this)
                    }

                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        hasFixedSize(true)
                        withModelsFrom(item.data.itemList!!) {
                            buildBannerCard(callbacks, it, i++, true)
                        }
                    }
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
                    buildDynamicInfoCard(callbacks, ctx, item, i++)
                            .addIf(isDynamicInfoCard(item), this)
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