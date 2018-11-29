package com.mor.eye.view.home.search

import com.airbnb.epoxy.TypedEpoxyController
import com.mor.eye.queryItem

class RecentSearchController(private val listener: (query: String) -> Unit) : TypedEpoxyController<List<String>>() {
    override fun buildModels(data: List<String>) {
        data.map { query ->
            queryItem {
                id(query)
                queryText(query)
                queryClick { _, _, _, _ ->
                    listener(query)
                }
            }
        }
    }
}