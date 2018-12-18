package com.mor.eye.view.home.community

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.other.dpToPx
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class CommunityEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private val spacing = ctx.dpToPx(8)
    private val leftPx = ctx.dpToPx(16)
    private val rightPx = ctx.dpToPx(16)

    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i = 0L

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {
            loadData.forEach { item ->

                if (isSquareCardCollection(item)) {
                    buildHeaderTextCard(callbacks, ctx, i++, item.data.header?.title, !item.data.header?.actionUrl.isNullOrBlank())
                            .addTo(this)

                    carousel {
                        id(i++)
                        padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                        withModelsFrom(item.data.itemList!!) {
                            buildBannerCard(callbacks, it, i++, true)
                        }
                    }
                }

                if (isPictureFollowCard(item)) {
                    buildPictureFollow(callbacks, ctx, item, i++)
                            .addTo(this)
                }

                if (isAutoPlayFollowCard(item)) {
                    buildAutoVideo(callbacks, ctx, item, i++)
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
}
