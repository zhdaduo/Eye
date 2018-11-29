package com.mor.eye.repository

import com.mor.eye.repository.data.*
import com.mor.eye.repository.local.*
import com.mor.eye.util.other.AuthorTabConstant.Companion.AUTHOR_DYNAMICS
import com.mor.eye.util.other.AuthorTabConstant.Companion.AUTHOR_INDEX
import com.mor.eye.util.other.AuthorTabConstant.Companion.AUTHOR_PLAY_LIST
import com.mor.eye.util.other.AuthorTabConstant.Companion.AUTHOR_WORKS
import com.mor.eye.util.other.DataSourceConstant.Companion.COM_START
import com.mor.eye.util.other.DataSourceConstant.Companion.DAY_DATE
import com.mor.eye.util.other.DataSourceConstant.Companion.DAY_NUM
import com.mor.eye.util.other.DataSourceConstant.Companion.FIND_NUM
import com.mor.eye.util.other.DataSourceConstant.Companion.FIND_START
import com.mor.eye.util.other.DataSourceConstant.Companion.NUM
import com.mor.eye.util.other.DataSourceConstant.Companion.PAGE
import com.mor.eye.util.other.DataSourceConstant.Companion.START
import com.mor.eye.util.other.DataSourceConstant.Companion.TAB_NUM
import com.mor.eye.util.other.DataSourceConstant.Companion.TAB_START
import com.mor.eye.util.other.TagTabConstant.Companion.TAG_DYNAMICS
import com.mor.eye.util.other.TagTabConstant.Companion.TAG_VIDEOS
import io.reactivex.Single

interface EyeRepository {

    /* local */
    fun insertSearch(recentSearch: RecentSearch)

    fun getRecentSearch(): Single<List<RecentSearch>>
    fun deleteSearch()
    fun getCategoryOrder(): List<CategoryOrder>
    fun updateCategory(categoryOrders: List<CategoryOrder>)
    fun insertActiveTab(activeTab: ActiveTab)
    fun getActiveTab(): Single<List<ActiveTab>>
    fun deleteActiveTab()

    /* remote */
    fun getAllVideo(): Single<VideoBean>

    fun getCommonTabData(order: Int): Single<FindBean>
    fun getRecommendData(): Single<FindBean>
    fun getMoreRecommendData(page: String, isTag: String, adIndex: String): Single<FindBean>
    fun getMoreCommonTabData(stringHashMap: HashMap<String, String?>, position: Int): Single<FindBean>
    fun getSearchHotKeyWord(): Single<List<String>>
    fun getQueryData(query: String): Single<FindBean>
    fun getMoreQueryData(start: String, num: String, query: String): Single<FindBean>
    fun getAllCategory(): Single<FindBean>
    fun getCategoryTabDetailData(id: String): Single<CategoryDetailBean>
    fun getCategoryDetailData(position: Int, id: String): Single<FindBean>
    fun getMoreCategoryDetailData(position: Int, id: String, stringHashMap: HashMap<String, String?>): Single<FindBean>
    fun getAuthorTabDetailData(id: String, userType: String): Single<AuthorDetailBean>
    fun getAuthorDetailData(tabType: String, id: String, userType: String): Single<FindBean>
    fun getMoreAuthorDetailData(tabType: String, id: String, userType: String, stringHashMap: HashMap<String, String?>): Single<FindBean>
    fun getTagTabDetailData(id: String): Single<TagsDetailBean>
    fun getTagDetailData(tabType: String, id: String): Single<FindBean>
    fun getMoreTagDetailData(tabType: String, id: String, stringHashMap: HashMap<String, String?>): Single<FindBean>
    fun getDetailRecommendData(id: String): Single<Relate>
    fun getHomeTabList(): Single<TabInfo>
    fun getVideoDetailData(id: String): Single<Video>
}

class WeatherRepositoryImpl(
        private val eyeDataSource: EyeDataSource,
        private val recentSearchDao: RecentSearchDao,
        private val categoryOrderDao: CategoryOrderDao,
        private val activeTabDao: ActiveTabDao
) : EyeRepository {
    override fun getRecommendData(): Single<FindBean> = eyeDataSource.getRecommendData()

    override fun getMoreRecommendData(page: String, isTag: String, adIndex: String): Single<FindBean> {
        return if (isTag == "true") eyeDataSource.getRecommendData(page, isTag, adIndex) else eyeDataSource.getRecommendData2(page, isTag)
    }

    override fun getVideoDetailData(id: String): Single<Video> = eyeDataSource.getVideoDetailData(id)

    override fun getHomeTabList(): Single<TabInfo> = eyeDataSource.getHomeTabList()

    override fun insertActiveTab(activeTab: ActiveTab) = activeTabDao.insertActiveTab(activeTab)

    override fun getActiveTab(): Single<List<ActiveTab>> = activeTabDao.getActiveTab()

    override fun deleteActiveTab() = activeTabDao.deleteActiveTab()

    override fun getDetailRecommendData(id: String): Single<Relate> = eyeDataSource.getDetailRecommendData(id)

    override fun getTagTabDetailData(id: String): Single<TagsDetailBean> = eyeDataSource.getTagIndexData(id)

    override fun getTagDetailData(tabType: String, id: String): Single<FindBean> = when (tabType) {
        TAG_VIDEOS -> eyeDataSource.getTagVideoData(id)
        TAG_DYNAMICS -> eyeDataSource.getTagDynamicsData(id)
        else -> throw IllegalArgumentException("Wrong position of Tag Tab")
    }

    override fun getMoreTagDetailData(tabType: String, id: String, stringHashMap: HashMap<String, String?>): Single<FindBean> = when (tabType) {
        TAG_VIDEOS -> eyeDataSource.getMoreTagVideoData(id, stringHashMap[START]!!, stringHashMap[NUM]!!, "date")
        TAG_DYNAMICS -> eyeDataSource.getMoreTagDynamicsData(id, stringHashMap[START]!!, stringHashMap[NUM]!!, "date")
        else -> throw IllegalArgumentException("Wrong position of Tag Tab")
    }

    override fun getAuthorTabDetailData(id: String, userType: String): Single<AuthorDetailBean> = eyeDataSource.getAuthorDetailIndexData(id, userType)

    override fun getAuthorDetailData(tabType: String, id: String, userType: String): Single<FindBean> = when (tabType) {
        AUTHOR_INDEX -> eyeDataSource.getAuthorIndexData(id, userType)
        AUTHOR_WORKS -> eyeDataSource.getAuthorWorksData(id)
        AUTHOR_PLAY_LIST -> eyeDataSource.getAuthorPlayListData(id)
        AUTHOR_DYNAMICS -> eyeDataSource.getAuthorDynamicsData(id, userType)
        else -> throw IllegalArgumentException("Wrong position of Author Tab")
    }

    override fun getMoreAuthorDetailData(tabType: String, id: String, userType: String, stringHashMap: HashMap<String, String?>): Single<FindBean> = when (tabType) {
        AUTHOR_WORKS -> eyeDataSource.getMoreAuthorWorksData(id, stringHashMap[START]!!, stringHashMap[NUM]!!)
        AUTHOR_PLAY_LIST -> eyeDataSource.getMoreAuthorPlayListData(id, stringHashMap[START]!!, stringHashMap[NUM]!!)
        AUTHOR_DYNAMICS -> eyeDataSource.getMoreAuthorDynamicsData(id, userType, stringHashMap[START]!!, stringHashMap[NUM]!!)
        else -> throw IllegalArgumentException("Wrong position of Author Tab")
    }

    override fun getCategoryTabDetailData(id: String): Single<CategoryDetailBean> = eyeDataSource.getCategoryTabDetailData(id)

    override fun getCategoryDetailData(position: Int, id: String): Single<FindBean> = when (position) {
        0 -> eyeDataSource.getCategoryIndexData(id)
        1 -> eyeDataSource.getCategoryPgcsData(id)
        2 -> eyeDataSource.getCategoryVideolistData(id)
        3 -> eyeDataSource.getCategoryPlaylistData(id)
        else -> throw IllegalArgumentException("Wrong position of Category Tab")
    }

    override fun getMoreCategoryDetailData(position: Int, id: String, stringHashMap: HashMap<String, String?>): Single<FindBean> = when (position) {
        0 -> eyeDataSource.getMoreCategoryIndexData(id, stringHashMap[PAGE]!!)
        1 -> eyeDataSource.getMoreCategoryPgcsData(id, stringHashMap[START]!!, stringHashMap[NUM]!!)
        2 -> eyeDataSource.getMoreCategoryVideolistData(id, stringHashMap[START]!!, stringHashMap[NUM]!!)
        3 -> eyeDataSource.getMoreCategoryPlaylistData(id, stringHashMap[START]!!, stringHashMap[NUM]!!)
        else -> throw IllegalArgumentException("Wrong position of Category Tab")
    }

    override fun getCategoryOrder(): List<CategoryOrder> = categoryOrderDao.getCategoryOrder()

    override fun updateCategory(categoryOrders: List<CategoryOrder>) = categoryOrderDao.updateCategory(categoryOrders)

    override fun getAllCategory(): Single<FindBean> = eyeDataSource.getAllCategoryData()

    override fun insertSearch(recentSearch: RecentSearch) = recentSearchDao.insertSearch(recentSearch)

    override fun getRecentSearch(): Single<List<RecentSearch>> = recentSearchDao.getRecentSearch()

    override fun deleteSearch() = recentSearchDao.deleteSearch()

    override fun getQueryData(query: String): Single<FindBean> = eyeDataSource.getQueryData(query)

    override fun getMoreQueryData(start: String, num: String, query: String): Single<FindBean> = eyeDataSource.getMoreQueryData(start, num, query)

    override fun getSearchHotKeyWord(): Single<List<String>> = eyeDataSource.getSearchHotKeyWord()

    override fun getAllVideo(): Single<VideoBean> = eyeDataSource.getAllVideo()

    override fun getCommonTabData(order: Int): Single<FindBean> = when (order) {
        -1 -> eyeDataSource.getFindData()
        -2 -> eyeDataSource.getRecommendData()
        -3 -> eyeDataSource.getDailyData()
        -4 -> eyeDataSource.getCommunityData()
        2 -> eyeDataSource.getCreativeData()
        4 -> eyeDataSource.getAppetizerData()
        6 -> eyeDataSource.getTravelData()
        8 -> eyeDataSource.getTrailerData()
        10 -> eyeDataSource.getAnimData()
        12 -> eyeDataSource.getPlotData()
        14 -> eyeDataSource.getAdvertData()
        18 -> eyeDataSource.getSportsData()
        20 -> eyeDataSource.getMusicData()
        22 -> eyeDataSource.getRecordData()
        24 -> eyeDataSource.getFashionData()
        26 -> eyeDataSource.getCuteData()
        28 -> eyeDataSource.getFunnyData()
        30 -> eyeDataSource.getGameData()
        32 -> eyeDataSource.getScienceData()
        34 -> eyeDataSource.getHighlightsData()
        36 -> eyeDataSource.getLifeData()
        38 -> eyeDataSource.getArtsData()
        else -> throw IllegalArgumentException("Wrong position of Home Tab")
    }

    override fun getMoreCommonTabData(stringHashMap: HashMap<String, String?>, position: Int): Single<FindBean> =
            when (position) {
                -1 -> {
                    if (stringHashMap[FIND_NUM] == "null") {
                        eyeDataSource.getFindData2(stringHashMap[FIND_START]!!)
                    } else {
                        eyeDataSource.getFindData(stringHashMap[FIND_START]!!, stringHashMap[FIND_NUM]!!)
                    }
                }

                -3 -> eyeDataSource.getDailyData(stringHashMap[DAY_DATE]!!, stringHashMap[DAY_NUM]!!)
                -4 -> eyeDataSource.getCommunityData(stringHashMap[COM_START]!!)
                2 -> eyeDataSource.getCreativeData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                4 -> eyeDataSource.getAppetizerData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                6 -> eyeDataSource.getTravelData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                8 -> eyeDataSource.getTrailerData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                10 -> eyeDataSource.getAnimData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                12 -> eyeDataSource.getPlotData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                14 -> eyeDataSource.getAdvertData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                18 -> eyeDataSource.getSportsData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                20 -> eyeDataSource.getMusicData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                22 -> eyeDataSource.getRecordData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                24 -> eyeDataSource.getFashionData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                26 -> eyeDataSource.getCuteData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                28 -> eyeDataSource.getFunnyData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                30 -> eyeDataSource.getGameData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                32 -> eyeDataSource.getScienceData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                34 -> eyeDataSource.getHighlightsData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                36 -> eyeDataSource.getLifeData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                38 -> eyeDataSource.getArtsData(stringHashMap[TAB_START]!!, stringHashMap[TAB_NUM]!!)
                else -> throw IllegalArgumentException("Wrong position of Home Tab")
            }
}