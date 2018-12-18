package com.mor.eye.view.home.category

import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.view.base.AbstractEpoxyController
import com.mor.eye.view.base.Callbacks

class CategoryController(val callbacks: Callbacks) : AbstractEpoxyController() {
    private var loadData = mutableListOf<ItemListBean>()

    override fun buildModels() {
        var i = 0L

        loadData.forEach { item ->
            buildBriefCard(callbacks, item, i++, true)
                    .addTo(this)
        }
    }

    fun addLoadData(loadItems: List<ItemListBean>) {
        loadData.addAll(loadItems)
        requestModelBuild()
    }
}