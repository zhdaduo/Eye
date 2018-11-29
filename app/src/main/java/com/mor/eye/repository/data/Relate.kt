package com.mor.eye.repository.data

data class Relate(
        val itemList: List<ItemList>,
        val count: Long,
        val total: Long,
        val nextPageUrl: Any? = null,
        val adExist: Boolean
) {

    data class ItemList(
            val type: String,
            val data: Data,
            val tag: Any? = null,
            val id: Long,
            val adIndex: Long
    )

    data class Data(
            val dataType: String,
            val id: Long,
            val type: String,
            val text: String? = null,
            val subTitle: Any? = null,
            val actionUrl: String? = null,
            val adTrack: Any? = null,
            val follow: Any? = null,
            val title: String? = null,
            val description: String? = null,
            val library: String? = null,
            val tags: List<Tag>? = null,
            val consumption: Consumption? = null,
            val resourceType: String? = null,
            val slogan: Any? = null,
            val provider: Provider? = null,
            val category: String? = null,
            val author: Author? = null,
            val cover: Cover? = null,
            val playUrl: String? = null,
            val thumbPlayUrl: Any? = null,
            val duration: Long? = null,
            val webUrl: WebURL? = null,
            val releaseTime: Long? = null,
            val playInfo: List<PlayInfo>? = null,
            val campaign: Any? = null,
            val waterMarks: Any? = null,
            val ad: Boolean? = null,
            val titlePgc: String? = null,
            val descriptionPgc: String? = null,
            val remark: String? = null,
            val ifLimitVideo: Boolean? = null,
            val searchWeight: Long? = null,
            val idx: Long? = null,
            val shareAdTrack: Any? = null,
            val favoriteAdTrack: Any? = null,
            val webAdTrack: Any? = null,
            val date: Long? = null,
            val promotion: Any? = null,
            val label: Any? = null,
            val labelList: List<Any?>? = null,
            val descriptionEditor: String? = null,
            val collected: Boolean? = null,
            val played: Boolean? = null,
            val subtitles: List<Any?>? = null,
            val lastViewTime: Any? = null,
            val playlists: Any? = null,
            val src: Any? = null
    )

    data class Author(
            val id: Long,
            val icon: String,
            val name: String,
            val description: String,
            val link: String,
            val latestReleaseTime: Long,
            val videoNum: Long,
            val adTrack: Any? = null,
            val follow: Follow,
            val shield: Shield,
            val approvedNotReadyVideoCount: Long,
            val ifPgc: Boolean
    )

    data class Follow(
            val itemType: String,
            val itemId: Long,
            val followed: Boolean
    )

    data class Shield(
            val itemType: String,
            val itemId: Long,
            val shielded: Boolean
    )

    data class Consumption(
            val collectionCount: Long,
            val shareCount: Long,
            val replyCount: Long
    )

    data class Cover(
            val feed: String,
            val detail: String,
            val blurred: String,
            val sharing: Any? = null,
            val homepage: Any? = null
    )

    data class PlayInfo(
            val height: Long,
            val width: Long,
            val urlList: List<URLList>,
            val name: String,
            val type: String,
            val url: String
    )

    data class URLList(
            val name: String,
            val url: String,
            val size: Long
    )

    data class Provider(
            val name: String,
            val alias: String,
            val icon: String
    )

    data class Tag(
            val id: Long,
            val name: String,
            val actionUrl: String,
            val adTrack: Any? = null,
            val desc: Any? = null,
            val bgPicture: String,
            val headerImage: String,
            val tagRecType: String,
            val childTagList: Any? = null,
            val childTagIdList: Any? = null,
            val communityIndex: Long
    )

    data class WebURL(
            val raw: String,
            val forWeibo: String
    )
}