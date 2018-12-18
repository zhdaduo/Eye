package com.mor.eye.repository

import com.mor.eye.repository.data.VideoBean
import com.mor.eye.repository.data.FindBean
import com.mor.eye.repository.data.CategoryDetailBean
import com.mor.eye.repository.data.AuthorDetailBean
import com.mor.eye.repository.data.TagsDetailBean
import com.mor.eye.repository.data.TabInfo
import com.mor.eye.repository.data.Video
import com.mor.eye.repository.local.RecentSearch
import com.mor.eye.repository.local.CategoryOrder
import com.mor.eye.repository.local.ActiveTab
import com.mor.eye.repository.local.RecentSearchDao
import com.mor.eye.repository.local.CategoryOrderDao
import com.mor.eye.repository.local.ActiveTabDao
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
    fun getRecommendData(): Single<FindBean>
    fun getSearchHotKeyWord(): Single<List<String>>
    fun getQueryData(query: String): Single<FindBean>
    fun getAllCategory(): Single<FindBean>
    fun getCategoryTabDetailData(id: String): Single<CategoryDetailBean>
    fun getCategoryDetailData(position: Int, id: String): Single<FindBean>
    fun getAuthorTabDetailData(id: String, userType: String): Single<AuthorDetailBean>
    fun getAuthorDetailData(position: Int, id: String, userType: String): Single<FindBean>
    fun getTagTabDetailData(id: String): Single<TagsDetailBean>
    fun getTagDetailData(position: Int, id: String): Single<FindBean>
    fun getDetailRecommendData(id: String): Single<FindBean>
    fun getHomeTabList(): Single<TabInfo>
    fun getVideoDetailData(id: String): Single<Video>
    fun getCommonTabData(categoryType: Int): Single<FindBean>
    fun getLoadMoreData(url: String): Single<FindBean>
}

class WeatherRepositoryImpl(
    private val eyeDataSource: EyeDataSource,
    private val recentSearchDao: RecentSearchDao,
    private val categoryOrderDao: CategoryOrderDao,
    private val activeTabDao: ActiveTabDao
) : EyeRepository {

    override fun getRecommendData(): Single<FindBean> = eyeDataSource.getRecommendData()

    override fun getVideoDetailData(id: String): Single<Video> = eyeDataSource.getVideoDetailData(id)

    override fun getHomeTabList(): Single<TabInfo> = eyeDataSource.getHomeTabList()

    override fun insertActiveTab(activeTab: ActiveTab) = activeTabDao.insertActiveTab(activeTab)

    override fun getActiveTab(): Single<List<ActiveTab>> = activeTabDao.getActiveTab()

    override fun deleteActiveTab() = activeTabDao.deleteActiveTab()

    override fun getDetailRecommendData(id: String): Single<FindBean> = eyeDataSource.getDetailRecommendData(id)

    override fun getTagTabDetailData(id: String): Single<TagsDetailBean> = eyeDataSource.getTagIndexData(id)

    override fun getTagDetailData(position: Int, id: String): Single<FindBean> = when (position) {
        0 -> eyeDataSource.getTagVideoData(id)
        1 -> eyeDataSource.getTagDynamicsData(id)
        else -> throw IllegalArgumentException("Wrong position of Tag Tab")
    }

    override fun getAuthorTabDetailData(id: String, userType: String): Single<AuthorDetailBean> = eyeDataSource.getAuthorDetailIndexData(id, userType)

    override fun getAuthorDetailData(position: Int, id: String, userType: String): Single<FindBean> = when (position) {
        0 -> eyeDataSource.getAuthorIndexData(id, userType)
        1 -> eyeDataSource.getAuthorWorksData(id)
        2 -> eyeDataSource.getAuthorPlayListData(id)
        3 -> eyeDataSource.getAuthorDynamicsData(id, userType)
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

    override fun getCategoryOrder(): List<CategoryOrder> = categoryOrderDao.getCategoryOrder()

    override fun updateCategory(categoryOrders: List<CategoryOrder>) = categoryOrderDao.updateCategory(categoryOrders)

    override fun getAllCategory(): Single<FindBean> = eyeDataSource.getAllCategoryData()

    override fun insertSearch(recentSearch: RecentSearch) = recentSearchDao.insertSearch(recentSearch)

    override fun getRecentSearch(): Single<List<RecentSearch>> = recentSearchDao.getRecentSearch()

    override fun deleteSearch() = recentSearchDao.deleteSearch()

    override fun getQueryData(query: String): Single<FindBean> = eyeDataSource.getQueryData(query)

    override fun getSearchHotKeyWord(): Single<List<String>> = eyeDataSource.getSearchHotKeyWord()

    override fun getAllVideo(): Single<VideoBean> = eyeDataSource.getAllVideo()

    override fun getCommonTabData(categoryType: Int): Single<FindBean> = when (categoryType) {
        -1 -> eyeDataSource.getFindData()
        -2 -> eyeDataSource.getRecommendData()
        -3 -> eyeDataSource.getDailyData()
        -4 -> eyeDataSource.getCommunityData()
        else ->  eyeDataSource.getCommonTabData(categoryType)
    }

    override fun getLoadMoreData(url: String): Single<FindBean> = eyeDataSource.getLoadMoreData(url)
}