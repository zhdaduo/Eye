package com.mor.eye.repository

import com.mor.eye.repository.data.VideoBean
import com.mor.eye.repository.data.FindBean
import com.mor.eye.repository.data.TabInfo
import com.mor.eye.repository.data.Video
import com.mor.eye.repository.data.CategoryDetailBean
import com.mor.eye.repository.data.TagsDetailBean
import com.mor.eye.repository.data.AuthorDetailBean
import io.reactivex.Single
import retrofit2.http.*

interface EyeDataSource {

    companion object {
        const val FULL_DEVICE_QUERY = "?udid=462a0bf7f991461e9a8992e8fa65174cd4a9fc49&vc=411&vn=4.6&deviceModel=m3%20note&first_channel=eyepetizer_meizu_market&last_channel=eyepetizer_meizu_market&system_version_code=24"
    }

    @GET("v4/tabs/selected")
    fun getAllVideo(): Single<VideoBean>

    @GET("v3/queries/hot")
    fun getSearchHotKeyWord(): Single<List<String>>

    @GET("v3/search")
    fun getQueryData(@Query("query") query: String): Single<FindBean>

    @GET("v5/category/list")
    fun getAllCategoryData(): Single<FindBean>

    @GET("v5/index/tab/list")
    fun getHomeTabList(): Single<TabInfo>

    /* Video Detail Page */

    @GET("v2/video/{id}")
    fun getVideoDetailData(@Path("id")id: String): Single<Video>

    @GET("v4/video/related")
    fun getDetailRecommendData(@Query("id")id: String): Single<FindBean>

    /* Category Detail Page */

    @GET("v4/categories/detail/tab")
    fun getCategoryTabDetailData(@Query("id") id: String): Single<CategoryDetailBean>

    @GET("v4/categories/detail/index")
    fun getCategoryIndexData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/detail/pgcs")
    fun getCategoryPgcsData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/videoList")
    fun getCategoryVideolistData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/detail/playlist")
    fun getCategoryPlaylistData(@Query("id") id: String): Single<FindBean>

    /* Author Detail Page */

    @GET("v5/userInfo/tab$FULL_DEVICE_QUERY")
    fun getAuthorDetailIndexData(@Query("id") id: String, @Query("userType") userType: String): Single<AuthorDetailBean>

    @GET("v5/userInfo/tab/index")
    fun getAuthorIndexData(@Query("id") id: String, @Query("userType") userType: String): Single<FindBean>

    @GET("v5/userInfo/tab/works")
    fun getAuthorWorksData(@Query("uid") id: String): Single<FindBean>

    @GET("v4/pgcs/detail/playlist")
    fun getAuthorPlayListData(@Query("id") id: String): Single<FindBean>

    @GET("v5/userInfo/tab/dynamics")
    fun getAuthorDynamicsData(@Query("id") id: String, @Query("userType") userType: String): Single<FindBean>

    /* Tag Detail Page */

    @GET("v6/tag/index")
    fun getTagIndexData(@Query("id") id: String): Single<TagsDetailBean>

    @GET("v1/tag/videos")
    fun getTagVideoData(@Query("id") id: String): Single<FindBean>

    @GET("v6/tag/dynamics")
    fun getTagDynamicsData(@Query("id") id: String): Single<FindBean>

    /* HomePage load Data */

    @GET("v5/index/tab/discovery")
    fun getFindData(): Single<FindBean>

    @GET("v5/index/tab/allRec")
    fun getRecommendData(): Single<FindBean>

    @GET("v5/index/tab/feed")
    fun getDailyData(): Single<FindBean>

    @GET("v5/index/tab/ugcSelected")
    fun getCommunityData(): Single<FindBean>

    @GET("v5/index/tab/category/{categoryType}")
    fun getCommonTabData(@Path("categoryType") categoryType: Int): Single<FindBean>

    /* Load More Data*/

    @GET
    fun getLoadMoreData(@Url url:String): Single<FindBean>
}