package com.mor.eye.view.home.search

import android.content.Context
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class SearchResultEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isLoadMore = false
    private var isEmpty = false
    private var isNoMore = false
    private var loadData = mutableListOf<ItemListBean>()
    override fun buildModels() {
        var i = 0L

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {

            loadData.forEach { item ->
                if (isTextCard(item) && item.data.text != "社区精选" && item.data.text != "查看全部热门排行") {
                    buildHeaderTextCard(callbacks, ctx, i++, item.data.text)
                            .addTo(this)
                }

                if (isBriefCard(item)) {
                    buildBriefCard(callbacks, item, i++, false)
                            .addTo(this)
                }

                if (isFollowCard(item)) {
                    buildFollowCard(callbacks, ctx, item, i++, false)
                            .addTo(this)
                }
            }
        }

        buildStatusLoading(i++).addIf(isLoadMore, this)

        buildStatusNoMore(i++).addIf(isNoMore, this)
    }

    fun addLoadData(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        isLoadMore = false
        requestModelBuild()
    }

    fun showEmpty() {
        isEmpty = true
        requestModelBuild()
    }

    fun showEnd() {
        isNoMore = true
        isLoadMore = false
        requestModelBuild()
    }

    fun showFooter() {
        isLoadMore = true
        requestModelBuild()
    }
}