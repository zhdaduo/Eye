package com.mor.eye.repository.data

data class ConfigsBean(
        val autoCache: AutoCache,
        val startPageAd: StartPageAd,
        val videoAdsConfig: Config,
        val startPage: CampaignInDetail,
        val log: Log,
        val issueCover: IssueCover,
        val startPageVideo: StartPageVideo,
        val consumption: Consumption,
        val launch: Launch,
        val preload: Preload,
        val version: String,
        val push: Push,
        val androidPlayer: AndroidPlayer,
        val profilePageAd: ProfilePageAd,
        val firstLaunch: FirstLaunch,
        val shareTitle: ShareTitle,
        val campaignInDetail: CampaignInDetail,
        val campaignInFeed: CampaignInDetail,
        val reply: Preload,
        val startPageVideoConfig: Config,
        val pushInfo: PushInfo,
        val homepage: Homepage
)

data class AndroidPlayer(
        val playerList: List<String>,
        val version: String
)

data class AutoCache(
        val forceOff: Boolean,
        val videoNum: Long,
        val version: String,
        val offset: Long
)

data class CampaignInDetail(
        val imageUrl: String,
        val available: Boolean? = null,
        val actionUrl: String,
        val showType: String? = null,
        val version: String
)

data class Consumption(
        val display: Boolean,
        val version: String
)

data class FirstLaunch(
        val showFirstDetail: Boolean,
        val version: String
)

data class Homepage(
        val cover: Any? = null,
        val version: String
)

data class IssueCover(
        val issueLogo: IssueLogo,
        val version: String
)

data class IssueLogo(
        val weekendExtra: String,
        val adapter: Boolean
)

data class Launch(
        val version: String,
        val adTrack: List<Any?>
)

data class Log(
        val playLog: Boolean,
        val version: String
)

data class Preload(
        val version: String,
        val on: Boolean
)

data class ProfilePageAd(
        val imageUrl: String,
        val actionUrl: String,
        val startTime: Long,
        val endTime: Long,
        val version: String,
        val adTrack: List<Any?>
)

data class Push(
        val startTime: Long,
        val endTime: Long,
        val message: String,
        val version: String
)

data class PushInfo(
        val normal: Any? = null,
        val version: String
)

data class ShareTitle(
        val weibo: String,
        val wechatMoments: String,
        val qzone: String,
        val version: String
)

data class StartPageAd(
        val displayTimeDuration: Long,
        val showBottomBar: Boolean,
        val countdown: Boolean,
        val actionUrl: String,
        val blurredImageUrl: String,
        val canSkip: Boolean,
        val version: String,
        val imageUrl: String,
        val displayCount: Long,
        val startTime: Long,
        val endTime: Long,
        val adTrack: List<Any?>,
        val newImageUrl: String
)

data class StartPageVideo(
        val displayTimeDuration: Long,
        val showImageTime: Long,
        val actionUrl: String,
        val countdown: Boolean,
        val showImage: Boolean,
        val canSkip: Boolean,
        val version: String,
        val url: String,
        val loadingMode: Long,
        val displayCount: Long,
        val startTime: Long,
        val endTime: Long,
        val adTrack: List<Any?>,
        val timeBeforeSkip: Long
)

data class Config(
        val list: List<Any?>,
        val version: String
)