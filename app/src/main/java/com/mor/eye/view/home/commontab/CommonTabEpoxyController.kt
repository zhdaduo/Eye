package com.mor.eye.view.home.commontab

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.StringUtils
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.view.base.Callbacks

class CommonTabEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i: Long = 0
        if (isEmpty) {
            emptyState {
                id("empty_view")
            }
        } else {
            loadData.forEach { item ->
                if (item.type == ViewTypeConstant.TEXT_CARD) {
                    textHeader {
                        id(i++)
                        headerString(item.data.text)
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
}