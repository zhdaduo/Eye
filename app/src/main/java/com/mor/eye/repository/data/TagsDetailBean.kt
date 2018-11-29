package com.mor.eye.repository.data

data class TagsDetailBean(
        val tabInfo: TabInfoBean,
        val tagInfo: TagInfoBean

)

data class TabInfoBean(
        val defaultIdx: Int,
        val tabList: List<TabListBean>
)

data class TabListBean(
        val id: Int,
        val name: String,
        val apiUrl: String,
        val tabType: Int,
        val nameType: Int,
        val adTrack: Any?
)

data class TagInfoBean(
        val dataType: String,
        val id: Int,
        val name: String,
        val description: Any,
        val headerImage: String,
        val bgPicture: String,
        val actionUrl: Any,
        val recType: Int,
        val follow: TagFollowBean,
        val tagFollowCount: Int,
        val tagVideoCount: Int,
        val tagDynamicCount: Int
)

data class TagFollowBean(
        val itemType: String,
        val itemId: Int,
        val isFollowed: Boolean
)
