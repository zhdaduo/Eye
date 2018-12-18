package com.mor.eye.view.home.recommend

import android.content.Context
import com.airbnb.epoxy.Carousel
import com.mor.eye.R
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.DisplayUtils
import com.mor.eye.util.other.dpToPx
import com.mor.eye.util.epoxy.carousel
import com.mor.eye.util.epoxy.withModelsFrom
import com.mor.eye.util.ktx.bold
import com.mor.eye.util.ktx.buildSpannedString
import com.mor.eye.util.ktx.color
import com.mor.eye.util.ktx.size
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class RecommendEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
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

        val squareCardCollectionData = loadData.first { isSquareCardCollection(it) }
        val textCardData = loadMoreData.filter { isTextCard(it) }
        val followCardLoadMore = loadMoreData.filter { isFollowCard(it) }

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {
            val builder = buildSpannedString {
                bold {
                    color(DisplayUtils.getColor(ctx, R.color.tv_hint)) {
                        size(DisplayUtils.getDimension(ctx, R.dimen.sp_12)) {
                            append(squareCardCollectionData.data.header?.subTitle + "\n")
                        }
                    }
                }
                size(DisplayUtils.getDimension(ctx, R.dimen.sp_28)) {
                    append(squareCardCollectionData.data.header?.title)
                }
            }

            buildHeaderTextCard(callbacks, ctx, i++, builder)
                    .addTo(this)

            carousel {
                id(i++)
                padding(Carousel.Padding(0, 0, leftPx, rightPx, spacing))
                hasFixedSize(true)
                withModelsFrom(squareCardCollectionData.data.itemList!!) {
                    buildFollowCard(callbacks, ctx, it, i++, true)
                }
            }

            loadData.forEach { item ->
                if (isTextCard(item) && item.data.text != "社区精选" && item.data.text != "查看全部热门排行") {
                    val showHeaderArrow = if (item.data.text == "开眼编辑精选") false else !item.data.actionUrl.isNullOrBlank()
                    buildHeaderTextCard(callbacks, ctx, i++, item.data.text, showHeaderArrow)
                            .addTo(this)
                }

                if (isFollowCard(item)) {
                    buildFollowCard(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }

                if (isVideoSmallCard(item)) {
                    buildSmallCard(callbacks, ctx, item, i++)
                            .addTo(this)
                }

                if (isBanner2(item) || isBanner(item)) {
                    buildBannerCard(callbacks, item, i++, false)
                            .addTo(this)
                }
            }

            if (textCardData.isNotEmpty()) {
                buildHeaderTextCard(callbacks, ctx, i++, "猜你喜欢")
                        .addTo(this)

            }

            followCardLoadMore.map { item ->
                buildFollowCard(callbacks, ctx, item, i++, false)
                        .addTo(this)
            }

            buildStatusLoading(i++).addIf(isLoadMore, this)

            buildStatusNoMore(i++).addIf(isNoMore, this)
        }
    }

    fun update(loadItems: List<ItemListBean>) {
        loadData.clear()
        loadMoreData.clear()
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
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