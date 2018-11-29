package com.mor.eye.repository.data

data class CategoryDetailBean(
        val categoryInfo: CategoryInfoBean,
        val tabInfo: TabInfoBean
) {

    data class CategoryInfoBean(
            val dataType: String,
            val id: Int,
            val name: String,
            val description: String,
            val headerImage: String,
            val actionUrl: String,
            val follow: FollowBean
    ) {

        data class FollowBean(
                val itemType: String,
                val itemId: Int,
                val isFollowed: Boolean
        )
    }

    data class TabInfoBean(
            val defaultIdx: Int,
            val tabList: List<TabListBean>
    )

    data class TabListBean(
            val id: Int,
            val name: String,
            val apiUrl: String
    )
}