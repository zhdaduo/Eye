package com.mor.eye.view.home.commontab

import android.content.Context
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class CommonTabEpoxyController(private val ctx: Context, private val callbacks: Callbacks) : AbstractEpoxyController() {
    private var isLoadMore = false
    private var isNoMore = false
    private var isEmpty = false

    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i = 0L

        if (isEmpty) {
            buildStatusEmpty(i++).addTo(this)
        } else {
            loadData.forEach { item ->
                if (isTextCard(item)) {
                    buildHeaderTextCard(callbacks, ctx, i++, item.data.text)
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