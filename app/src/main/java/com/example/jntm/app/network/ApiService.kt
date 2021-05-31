package com.example.jntm.app.network

import com.example.jntm.data.model.bean.*
import retrofit2.http.*

interface ApiService {

    companion object {
        const val SERVER_URL = "https://wanandroid.com/"
    }

    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") pwd: String
    ): ApiResponse<UserInfo>

    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") pwd: String,
        @Field("repassword") rpwd: String
    ): ApiResponse<Any>

    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerResponse>>

    @GET("article/top/json")
    suspend fun getTopArticleList(): ApiResponse<ArrayList<AriticleResponse>>

    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("project/tree/json")
    suspend fun getProjectTitle(): ApiResponse<ArrayList<ClassifyResponse>>

    @GET("project/list/{page}/json")
    suspend fun getProjectDataByType(@Path("page") pageNo: Int, @Query("cid") cid: Int):
            ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("article/listproject/{page}/json")
    suspend fun getProjectNewData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("wxarticle/chapters/json")
    suspend fun getPublicTitle(): ApiResponse<ArrayList<ClassifyResponse>>

    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getPublicData(
        @Path("page") pageNo: Int,
        @Path("id") id: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("hotkey/json")
    suspend fun getSearchData(): ApiResponse<ArrayList<SearchResponse>>

    @POST("article/query/{page}/json")
    suspend fun getSearchDataByKey(
        @Path("page") pageNo: Int,
        @Query("k") searchKey: String
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("user_article/list/{page}/json")
    suspend fun getSquareData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("wenda/list/{page}/json")
    suspend fun getAskData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("tree/json")
    suspend fun getSystemData(): ApiResponse<ArrayList<SystemResponse>>

    @GET("article/list/{page}/json")
    suspend fun getSystemChildData(
        @Path("page") pageNo: Int,
        @Query("cid") cid: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>>

    @GET("navi/json")
    suspend fun getNavigationData(): ApiResponse<ArrayList<NavigationResponse>>

    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id: Int): ApiResponse<Any?>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun uncollect(@Path("id") id: Int): ApiResponse<Any?>

    @POST("lg/collect/addtool/json")
    suspend fun collectUrl(
        @Query("name") name: String,
        @Query("link") link: String
    ): ApiResponse<CollectUrlResponse>

    @POST("lg/collect/deletetool/json")
    suspend fun deletetool(@Query("id") id: Int): ApiResponse<Any?>

    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectData(@Path("page") pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<CollectResponse>>>

    @GET("lg/collect/usertools/json")
    suspend fun getCollectUrlData(): ApiResponse<ArrayList<CollectUrlResponse>>

    @GET("user/{id}/share_articles/{page}/json")
    suspend fun getShareByIdData(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): ApiResponse<ShareResponse>

    /**
     * 获取当前账户的个人积分
     */
    @GET("lg/coin/userinfo/json")
    suspend fun getIntegral(): ApiResponse<IntegralResponse>

    /**
     * 获取积分排行榜
     */
    @GET("coin/rank/{page}/json")
    suspend fun getIntegralRank(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralResponse>>>

    /**
     * 获取积分历史
     */
    @GET("lg/coin/list/{page}/json")
    suspend fun getIntegralHistory(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<IntegralHistoryResponse>>>

    /**
     * 获取自己分享的文章列表数据
     */
    @GET("user/lg/private_articles/{page}/json")
    suspend fun getShareData(@Path("page") page: Int): ApiResponse<ShareResponse>


    /**
     *  删除自己分享的文章
     */
    @POST("lg/user_article/delete/{id}/json")
    suspend fun deleteShareData(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 添加文章
     */
    @POST("lg/user_article/add/json")
    @FormUrlEncoded
    suspend fun addAriticle(
        @Field("title") title: String,
        @Field("link") content: String
    ): ApiResponse<Any?>

    /**
     * 获取Todo列表数据 根据完成时间排序
     */
    @GET("/lg/todo/v2/list/{page}/json")
    suspend fun getTodoData(@Path("page") page: Int): ApiResponse<ApiPagerResponse<ArrayList<TodoResponse>>>

    /**
     * 添加一个TODO
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    suspend fun addTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int
    ): ApiResponse<Any?>

    /**
     * 修改一个TODO
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    suspend fun updateTodo(
        @Field("title") title: String,
        @Field("content") content: String,
        @Field("date") date: String,
        @Field("type") type: Int,
        @Field("priority") priority: Int,
        @Path("id") id: Int
    ): ApiResponse<Any?>

    /**
     * 删除一个TODO
     */
    @POST("/lg/todo/delete/{id}/json")
    suspend fun deleteTodo(@Path("id") id: Int): ApiResponse<Any?>

    /**
     * 完成一个TODO
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    suspend fun doneTodo(@Path("id") id: Int, @Field("status") status: Int): ApiResponse<Any?>
}