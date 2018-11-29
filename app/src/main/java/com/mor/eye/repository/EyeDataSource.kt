package com.mor.eye.repository

import com.mor.eye.repository.data.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EyeDataSource {

    companion object {
        const val UDID = "?udid=5394cda42d75480cb47942affe0feb45339343a4"
        const val FULL_DEVICE_QUERY = "?udid=462a0bf7f991461e9a8992e8fa65174cd4a9fc49&vc=411&vn=4.6&deviceModel=m3%20note&first_channel=eyepetizer_meizu_market&last_channel=eyepetizer_meizu_market&system_version_code=24"
        const val RECOMMENDUDID = "?page=0&$UDID"
    }

    @GET("v4/tabs/selected$UDID")
    fun getAllVideo(): Single<VideoBean>

    @GET("v3/queries/hot$UDID")
    fun getSearchHotKeyWord(): Single<List<String>>

    @GET("v3/search$UDID")
    fun getQueryData(@Query("query") query: String): Single<FindBean>

    @GET("v3/search$UDID")
    fun getMoreQueryData(@Query("start") start: String, @Query("num") num: String, @Query("query") query: String): Single<FindBean>

    @GET("v5/category/list$UDID")
    fun getAllCategoryData(): Single<FindBean>

    @GET("v5/index/tab/list$UDID")
    fun getHomeTabList(): Single<TabInfo>

    /* Video Detail Page */

    @GET("v2/video/{id}$UDID")
    fun getVideoDetailData(@Path("id") id: String): Single<Video>

    @GET("v4/video/related$UDID")
    fun getDetailRecommendData(@Query("id") id: String): Single<Relate>

    /* Category Detail Page */

    @GET("v4/categories/detail/tab$UDID")
    fun getCategoryTabDetailData(@Query("id") id: String): Single<CategoryDetailBean>

    @GET("v4/categories/detail/index$UDID")
    fun getCategoryIndexData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/detail/index$UDID")
    fun getMoreCategoryIndexData(@Query("id") id: String, @Query("page") page: String): Single<FindBean>

    @GET("v4/categories/detail/pgcs$UDID")
    fun getCategoryPgcsData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/detail/pgcs$UDID")
    fun getMoreCategoryPgcsData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v4/categories/videoList$UDID")
    fun getCategoryVideolistData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/videoList$UDID")
    fun getMoreCategoryVideolistData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v4/categories/detail/playlist$UDID")
    fun getCategoryPlaylistData(@Query("id") id: String): Single<FindBean>

    @GET("v4/categories/detail/playlist$UDID")
    fun getMoreCategoryPlaylistData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    /* Author Detail Page */

    @GET("v5/userInfo/tab$FULL_DEVICE_QUERY")
    fun getAuthorDetailIndexData(@Query("id") id: String, @Query("userType") userType: String): Single<AuthorDetailBean>

    @GET("v5/userInfo/tab/index$UDID")
    fun getAuthorIndexData(@Query("id") id: String, @Query("userType") userType: String): Single<FindBean>

    @GET("v5/userInfo/tab/works$UDID")
    fun getAuthorWorksData(@Query("uid") id: String): Single<FindBean>

    @GET("v5/userInfo/tab/works$UDID")
    fun getMoreAuthorWorksData(@Query("uid") id: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v4/pgcs/detail/playlist$UDID")
    fun getAuthorPlayListData(@Query("id") id: String): Single<FindBean>

    @GET("v4/pgcs/detail/playlist$UDID")
    fun getMoreAuthorPlayListData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/userInfo/tab/dynamics$UDID")
    fun getAuthorDynamicsData(@Query("id") id: String, @Query("userType") userType: String): Single<FindBean>

    @GET("v5/userInfo/tab/dynamics$UDID")
    fun getMoreAuthorDynamicsData(@Query("id") id: String, @Query("userType") userType: String, @Query("start") start: String, @Query("num") num: String): Single<FindBean>

    /* Tag Detail Page */

    @GET("v6/tag/index$UDID")
    fun getTagIndexData(@Query("id") id: String): Single<TagsDetailBean>

    @GET("v1/tag/videos$UDID")
    fun getTagVideoData(@Query("id") id: String): Single<FindBean>

    @GET("v1/tag/videos$UDID")
    fun getMoreTagVideoData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String, @Query("strategy") strategy: String): Single<FindBean>

    @GET("v6/tag/dynamics$UDID")
    fun getTagDynamicsData(@Query("id") id: String): Single<FindBean>

    @GET("v6/tag/dynamics$UDID")
    fun getMoreTagDynamicsData(@Query("id") id: String, @Query("start") start: String, @Query("num") num: String, @Query("strategy") strategy: String): Single<FindBean>

    /* HomePage load Data */

    @GET("v5/index/tab/discovery$UDID")
    fun getFindData(): Single<FindBean>

    @GET("v5/index/tab/allRec$RECOMMENDUDID")
    fun getRecommendData(): Single<FindBean>

    @GET("v5/index/tab/feed$UDID")
    fun getDailyData(): Single<FindBean>

    @GET("v5/index/tab/ugcSelected$UDID")
    fun getCommunityData(): Single<FindBean>

    @GET("v5/index/tab/category/2$UDID")
    fun getCreativeData(): Single<FindBean>

    @GET("v5/index/tab/category/4$UDID")
    fun getAppetizerData(): Single<FindBean>

    @GET("v5/index/tab/category/6$UDID")
    fun getTravelData(): Single<FindBean>

    @GET("v5/index/tab/category/8$UDID")
    fun getTrailerData(): Single<FindBean>

    @GET("v5/index/tab/category/10$UDID")
    fun getAnimData(): Single<FindBean>

    @GET("v5/index/tab/category/12$UDID")
    fun getPlotData(): Single<FindBean>

    @GET("v5/index/tab/category/14$UDID")
    fun getAdvertData(): Single<FindBean>

    @GET("v5/index/tab/category/18$UDID")
    fun getSportsData(): Single<FindBean>

    @GET("v5/index/tab/category/20$UDID")
    fun getMusicData(): Single<FindBean>

    @GET("v5/index/tab/category/22$UDID")
    fun getRecordData(): Single<FindBean>

    @GET("v5/index/tab/category/24$UDID")
    fun getFashionData(): Single<FindBean>

    @GET("v5/index/tab/category/26$UDID")
    fun getCuteData(): Single<FindBean>

    @GET("v5/index/tab/category/28$UDID")
    fun getFunnyData(): Single<FindBean>

    @GET("v5/index/tab/category/30$UDID")
    fun getGameData(): Single<FindBean>

    @GET("v5/index/tab/category/32$UDID")
    fun getScienceData(): Single<FindBean>

    @GET("v5/index/tab/category/34$UDID")
    fun getHighlightsData(): Single<FindBean>

    @GET("v5/index/tab/category/36$UDID")
    fun getLifeData(): Single<FindBean>

    @GET("v5/index/tab/category/38$UDID")
    fun getArtsData(): Single<FindBean>

    /* HomePage load more Data */

    @GET("v5/index/tab/discovery$UDID")
    fun getFindData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/discovery$UDID")
    fun getFindData2(@Query("start") start: String): Single<FindBean>

    @GET("v5/index/tab/allRec$UDID")
    fun getRecommendData(@Query("page") page: String, @Query("isTag") isTag: String, @Query("adIndex") adIndex: String): Single<FindBean>

    @GET("v5/index/tab/allRec$UDID")
    fun getRecommendData2(@Query("page") page: String, @Query("isTag") isTag: String): Single<FindBean>

    @GET("v5/index/tab/feed$UDID")
    fun getDailyData(@Query("date") date: String, @Query("num") start: String): Single<FindBean>

    @GET("v5/index/tab/ugcSelected$UDID")
    fun getCommunityData(@Query("start") start: String): Single<FindBean>

    @GET("v5/index/tab/category/2$UDID")
    fun getCreativeData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/4$UDID")
    fun getAppetizerData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/6$UDID")
    fun getTravelData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/8$UDID")
    fun getTrailerData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/10$UDID")
    fun getAnimData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/12$UDID")
    fun getPlotData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/14$UDID")
    fun getAdvertData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/18$UDID")
    fun getSportsData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/20$UDID")
    fun getMusicData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/22$UDID")
    fun getRecordData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/24$UDID")
    fun getFashionData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/26$UDID")
    fun getCuteData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/28$UDID")
    fun getFunnyData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/30$UDID")
    fun getGameData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/32$UDID")
    fun getScienceData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/34$UDID")
    fun getHighlightsData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/36$UDID")
    fun getLifeData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>

    @GET("v5/index/tab/category/38$UDID")
    fun getArtsData(@Query("start") start: String, @Query("num") num: String): Single<FindBean>
}