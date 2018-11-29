package com.mor.eye.repository.data

data class Video(
        val dataType: String,
        val id: Long,
        val title: String,
        val description: String,
        val library: String,
        val tags: List<Tag>,
        val consumption: Consumption,
        val resourceType: String,
        val slogan: String,
        val provider: Provider,
        val category: String,
        val author: Author,
        val cover: Cover,
        val playUrl: String,
        val thumbPlayUrl: Any? = null,
        val duration: Long,
        val webUrl: WebURL,
        val releaseTime: Long,
        val playInfo: List<PlayInfo>,
        val campaign: Any? = null,
        val waterMarks: Any? = null,
        val ad: Boolean,
        val adTrack: Any? = null,
        val type: String,
        val titlePgc: Any? = null,
        val descriptionPgc: Any? = null,
        val remark: Any? = null,
        val ifLimitVideo: Boolean,
        val searchWeight: Long,
        val idx: Long,
        val shareAdTrack: Any? = null,
        val favoriteAdTrack: Any? = null,
        val webAdTrack: Any? = null,
        val date: Long,
        val promotion: Any? = null,
        val label: Any? = null,
        val labelList: List<Any?>,
        val descriptionEditor: String,
        val collected: Boolean,
        val played: Boolean,
        val subtitles: List<Any?>,
        val lastViewTime: Any? = null,
        val playlists: Any? = null,
        val src: Any? = null
) {

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
            val homepage: String
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