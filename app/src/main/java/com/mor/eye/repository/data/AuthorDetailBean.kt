package com.mor.eye.repository.data

data class AuthorDetailBean(
        val tabInfo: TabInfoBean,
        val pgcInfo: PgcInfoBean?,
        val userInfo: UserInfoBean?
) {
    data class UserInfoBean(
            val dataType: String,
            val id: Int,
            val icon: String,
            val name: String,
            val brief: String,
            val description: String,
            val actionUrl: String,
            val area: String,
            val gender: String,
            val registDate: Long,
            val followCount: Int,
            val follow: FollowBean,
            val self: Boolean,
            val cover: String,
            val videoCount: Int,
            val shareCount: Int,
            val collectCount: Int,
            val myFollowCount: Int,
            val videoCountActionUrl: String,
            val myFollowCountActionUrl: String,
            val followCountActionUrl: String
    )

    data class TabInfoBean(
            val defaultIdx: Int,
            val tabList: List<TabListBean>
    ) {
        data class TabListBean(
                val id: Int,
                val name: String,
                val apiUrl: String
        )
    }

    data class PgcInfoBean(
            val dataType: String,
            val id: Int,
            val icon: String,
            val name: String,
            val brief: String,
            val description: String,
            val actionUrl: String,
            val area: String,
            val gender: String,
            val registDate: Long,
            val followCount: Int,
            val follow: FollowBean,
            val isSelf: Boolean,
            val cover: String,
            val videoCount: Int,
            val shareCount: Int,
            val collectCount: Int,
            val shield: ShieldBean
    ) {

        data class ShieldBean(
                val itemType: String,
                val itemId: Int,
                val isShielded: Boolean
        )
    }

    data class FollowBean(
            val itemType: String,
            val itemId: Int,
            val isFollowed: Boolean
    )
}