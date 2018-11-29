package com.mor.eye.view.home.search

import android.content.Context
import com.airbnb.epoxy.EpoxyController
import com.mor.eye.*
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.util.StringUtils
import com.mor.eye.util.other.ViewTypeConstant
import com.mor.eye.util.other.showToast
import com.mor.eye.view.base.Callbacks

class SearchResultEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : EpoxyController() {
    private var showEmpty = false
    private var showEnd = false
    private var loadData = mutableListOf<ItemListBean>()
    override fun buildModels() {
        var i: Long = 0
        if (showEmpty) {
            emptyState {
                id("empty_view")
            }
        }
        if (showEnd) {
            textEnd {
                id("no_more_view")
            }
        }
        loadData.forEach { item ->
            if (item.type == ViewTypeConstant.TEXT_CARD && item.data.text != "社区精选" && item.data.text != "查看全部热门排行") {
                queryResultHeader {
                    id(i++)
                    headerText(item.data.text)
                    click { _ -> ctx.showToast("查看全部") }
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

            if (item.type == ViewTypeConstant.FOLLOW_CARD) {
                followCarousel {
                    id(i++)
                    timeText(StringUtils.durationFormat(item.data.content?.data?.duration!!))
                    titleText(item.data.header?.title)
                    descriptionText(String.format(ctx.getString(R.string.follow_description), item.data.content.data.author?.name, item.data.content.data.category))
                    feedUrl(item.data.content.data.cover?.feed)
                    coverUrl(item.data.header?.icon)
                    showAdIfTrue(item.type == ViewTypeConstant.BANNER3)
                    showTimeIfTrue(true)
                    authorClick { _ -> callbacks.onAuthorClickListener(item.data.content.data.author?.id!!, "PGC") }
                    videoClick { _ -> callbacks.onVideoClickListener(item.data.content.data.id) }
                }
            }
        }
    }

    fun addLoadData(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        requestModelBuild()
    }

    fun showEmpty() {
        showEmpty = true
        requestModelBuild()
    }

    fun showEnd() {
        showEnd = true
        requestModelBuild()
    }
}