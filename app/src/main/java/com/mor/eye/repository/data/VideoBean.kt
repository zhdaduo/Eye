package com.mor.eye.repository.data

class VideoBean {
    var count: Int = 0
    var total: Int = 0
    var nextPageUrl: String? = null
    var isAdExist: Boolean = false
    var date: Long = 0
    var nextPublishTime: Long = 0
    var dialog: Any? = null
    var topIssue: Any? = null
    var refreshCount: Int = 0
    var lastStartId: Int = 0
    var itemList: List<ItemListBeanX>? = null

    class ItemListBeanX {
        var type: String? = null
        var data: DataBean? = null
        var tag: String? = null
        var id: Int = 0

        class DataBean {
            var dataType: String? = null
            var id: Int = 0
            var title: String? = null
            var slogan: String? = null
            var description: String? = null
            var provider: ProviderBean? = null
            var category: String? = null
            var author: AuthorBean? = null
            var cover: CoverBean? = null
            var playUrl: String? = null
            var thumbPlayUrl: String? = null
            var duration: Int = 0
            var webUrl: WebUrlBean? = null
            var releaseTime: Long = 0
            var library: String? = null
            var consumption: ConsumptionBean? = null
            var campaign: Any? = null
            var waterMarks: Any? = null
            var adTrack: Any? = null
            var type: String? = null
            var titlePgc: String? = null
            var descriptionPgc: String? = null
            var remark: Any? = null
            var idx: Int = 0
            var shareAdTrack: Any? = null
            var favoriteAdTrack: Any? = null
            var webAdTrack: Any? = null
            var date: Long = 0
            var promotion: Any? = null
            var label: Any? = null
            var descriptionEditor: String? = null
            var isCollected: Boolean = false
            var isPlayed: Boolean = false
            var lastViewTime: Any? = null
            var playlists: Any? = null
            var playInfo: List<PlayInfoBean>? = null
            var tags: List<TagsBean>? = null
            var labelList: List<*>? = null
            var subtitles: List<*>? = null

            class ProviderBean {
                var name: String? = null
                var alias: String? = null
                var icon: String? = null
            }

            class AuthorBean {
                var id: Int = 0
                var icon: String? = null
                var name: String? = null
                var description: String? = null
                var link: String? = null
                var latestReleaseTime: Long = 0
                var videoNum: Int = 0
                var adTrack: Any? = null
                var follow: FollowBean? = null
                var shield: ShieldBean? = null
                var approvedNotReadyVideoCount: Int = 0
                var isIfPgc: Boolean = false

                class FollowBean {
                    var itemType: String? = null
                    var itemId: Int = 0
                    var isFollowed: Boolean = false
                }

                class ShieldBean {
                    var itemType: String? = null
                    var itemId: Int = 0
                    var isShielded: Boolean = false
                }
            }

            class CoverBean {
                var feed: String? = null
                var detail: String? = null
                var blurred: String? = null
                var sharing: Any? = null
                var homepage: String? = null
            }

            class WebUrlBean {
                var raw: String? = null
                var forWeibo: String? = null
            }

            class ConsumptionBean {
                var collectionCount: Int = 0
                var shareCount: Int = 0
                var replyCount: Int = 0
            }

            class PlayInfoBean {
                var height: Int = 0
                var width: Int = 0
                var name: String? = null
                var type: String? = null
                var url: String? = null
                var urlList: List<UrlListBean>? = null

                class UrlListBean {
                    var name: String? = null
                    var url: String? = null
                    var size: Int = 0
                }
            }

            class TagsBean {
                var id: Int = 0
                var name: String? = null
                var actionUrl: String? = null
                var adTrack: Any? = null
                var desc: Any? = null
                var bgPicture: String? = null
                var headerImage: String? = null
                var tagRecType: String? = null
            }
        }
    }
}
