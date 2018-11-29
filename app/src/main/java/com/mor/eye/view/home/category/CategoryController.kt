package com.mor.eye.view.home.category

import com.airbnb.epoxy.TypedEpoxyController
import com.mor.eye.briefCard
import com.mor.eye.repository.data.ItemListBean
import com.mor.eye.view.base.Callbacks

class CategoryController(val callbacks: Callbacks) : TypedEpoxyController<List<ItemListBean>>() {
    override fun buildModels(data: List<ItemListBean>) {
        data.forEach { item ->
            briefCard {
                id(item.data.id)
                pictureUrl(item.data.icon)
                titleText(item.data.title)
                descriptionText(item.data.description)
                showCategoryIfTrue(true)
                showFocusIfTrue(false)
                categoryClick { _ -> callbacks.onCategoryClickListener(item.data.id, item.data.icon) }
            }
        }
    }
}