package com.hotpodata.baconforkotlin.network

import com.hotpodata.baconforkotlin.network.model.AccessTokenResponse
import com.hotpodata.baconforkotlin.network.model.Thing
import retrofit.http.*
import rx.Observable

/**
 * Created by jdrotos on 11/29/15.
 */
interface RedditApi {
    @GET("/r/{subreddit}/hot/.json")
    fun getSubReddit(@Path("subreddit") subreddit: String, @Query("after") after: String?): Observable<Thing>

    /**
     * The cachedistraction argument is to thwart the cache when making multiple calls to /random.
     * This app depends on things actually being random, so though I'm sure some ops guy is mad about it,
     * being able to throw random junk is necessary. (and yes I tried using header settings to avoid the cache)
     */
    @GET("/r/{subreddit}/random/.json")
    fun getRandomSubredditPost(@Path("subreddit") subreddit: String, @Query("cachedistraction") depth: String?): Observable<MutableList<Thing>>

    @GET("/r/{subreddit}/comments/{article}/.json")
    fun getArticleComments(@Path("subreddit") subreddit: String, @Path("article") articleId: String, @Query("depth") depth: Int?, @Query("limit") limit: Int?): Observable<MutableList<Thing>>

    @GET("/api/v1/authorize?client_id=CLIENT_ID&response_type=TYPE&%20state=RANDOM_STRING&redirect_uri=URI&duration=DURATION&scope=SCOPE_STRING")
    fun authorize(@Query("client_id") clientId: String, @Query("response_type") responseType: String, @Query("state") state: String, @Query("redirect_uri") redirectUri: String, @Query("duration") duration: Long, @Query("scope") scope: String)

    @FormUrlEncoded
    @POST("/api/v1/access_token")
    fun accessToken(@Field("grant_type") grantType: String, @Field("device_id") deviceId: String): Observable<AccessTokenResponse>
}