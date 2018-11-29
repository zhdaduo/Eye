package com.mor.eye.repository.data

data class FindBean(
        val count: Int,
        val total: Int,
        val nextPageUrl: String?,
        val isAdExist: Boolean,
        val itemList: MutableList<ItemListBean>
)

data class ItemListBean(
        val type: String,
        val data: DataBean,
        val tag: Any,
        val id: Int,
        val adIndex: Int
)

data class DataBean(
        val icon: String,
        val actionUrl: String?,
        val text: String,
        val header: HeaderBean?,
        val content: ContentBean?,
        val dataType: String,
        val id: Int,
        val title: String,
        val slogan: String,
        val description: String,
        val provider: ProviderBean?,
        val category: String,
        val author: AuthorBean?,
        val cover: CoverBean?,
        val playUrl: String,
        val thumbPlayUrl: Any,
        val duration: Int,
        val webUrl: WebUrlBean?,
        val releaseTime: Long,
        val library: String,
        val consumption: ConsumptionBean?,
        val campaign: Any,
        val waterMarks: Any,
        val adTrack: Any,
        val type: String,
        val titlePgc: String,
        val descriptionPgc: String,
        val remark: Any,
        val idx: Int,
        val shareAdTrack: Any,
        val favoriteAdTrack: Any,
        val webAdTrack: Any,
        val date: Long,
        val promotion: Any,
        val label: LabelBean?,
        val descriptionEditor: String,
        val isCollected: Boolean,
        val isPlayed: Boolean,
        val lastViewTime: Any,
        val playlists: Any,
        val src: Any,
        val playInfo: List<PlayInfoBean>?,
        val tags: List<TagsBean>?,
        val labelList: List<*>,
        val subtitles: List<*>,
        val count: Int,
        val itemList: List<ItemListBean>?,
        val image: String,
        val isShade: Boolean,
        val user: UserBean,
        val createDate: Long,
        val simpleVideo: SimpleVideoBean?,
        val reply: ReplyBean?,
        val owner: OwnerBean?,
        val urls: List<*>,
        val area: Any? = null,
        val briefCard: BriefCardBean?
)

data class BriefCardBean(
        val dataType: String,
        val id: Int,
        val icon: String,
        val iconType: String,
        val title: String,
        val subTitle: String?,
        val description: String,
        val actionUrl: String,
        val adTrack: Any?,
        val follow: FollowBean,
        val ifPgc: Boolean
)

data class HeaderBean(
        val id: Int,
        val title: String,
        val font: String,
        val subTitle: String,
        val subTitleFont: Any,
        val textAlign: String,
        val cover: Any,
        val label: Any,
        val actionUrl: String,
        val labelList: Any,
        val icon: String,
        val iconType: String,
        val description: String,
        val time: Long,
        val isShowHateVideo: Boolean
)

data class ContentBean(
        val type: String,
        val data: DataBean,
        val tag: Any,
        val id: Int,
        val adIndex: Int
)

data class UserBean(
        val uid: Int,
        val nickname: String,
        val avatar: String,
        val userType: String,
        val isIfPgc: Boolean,
        val description: String,
        val area: Any,
        val gender: String,
        val registDate: Long,
        val cover: String,
        val actionUrl: String
)

data class OwnerBean(
        val uid: Int,
        val nickname: String,
        val avatar: String,
        val userType: String,
        val isIfPgc: Boolean,
        val description: String,
        val area: Any,
        val city: Any? = null,
        val gender: String,
        val registDate: Long,
        val releaseDate: Long? = null,
        val cover: String,
        val actionUrl: String,
        val followed: Boolean,
        val limitVideoOpen: Boolean,
        val library: String,
        val uploadStatus: String,
        val bannedDate: Any,
        val bannedDays: Any
)

data class SimpleVideoBean(
        val id: Int,
        val title: String,
        val description: String,
        val cover: CoverBean,
        val category: String,
        val playUrl: String,
        val duration: Int,
        val releaseTime: Long
)

data class CoverBean(
        val feed: String,
        val detail: String,
        val blurred: String,
        val sharing: Any,
        val homepage: String
)

data class ReplyBean(
        val id: Long,
        val videoId: Int,
        val videoTitle: String,
        val message: String,
        val likeCount: Int?,
        val isShowConversationButton: Boolean,
        val parentReplyId: Long,
        val rootReplyId: Long
)

data class ProviderBean(
        val name: String,
        val alias: String,
        val icon: String

)

data class AuthorBean(
        val id: Int,
        val icon: String,
        val name: String,
        val description: String,
        val link: String,
        val latestReleaseTime: Long,
        val videoNum: Int,
        val adTrack: Any,
        val follow: FollowBean,
        val shield: ShieldBean,
        val approvedNotReadyVideoCount: Int,
        val isIfPgc: Boolean
)

data class FollowBean(
        val itemType: String,
        val itemId: Int,
        val isFollowed: Boolean
)

data class ShieldBean(
        val itemType: String,
        val itemId: Int,
        val isShielded: Boolean
)

data class WebUrlBean(
        val raw: String,
        val forWeibo: String
)

data class ConsumptionBean(
        val collectionCount: Int,
        val shareCount: Int,
        val replyCount: Int
)

data class PlayInfoBean(
        val height: Int,
        val width: Int,
        val name: String,
        val type: String,
        val url: String,
        val urlList: List<UrlListBean>
)

data class UrlListBean(
        val name: String,
        val url: String,
        val size: Int
)

data class TagsBean(
        val id: Int,
        val name: String,
        val actionUrl: String,
        val adTrack: Any,
        val desc: Any?,
        val bgPicture: String,
        val headerImage: String,
        val tagRecType: String
)

data class LabelBean(
        val text: String,
        val card: String,
        val detail: Any
)
