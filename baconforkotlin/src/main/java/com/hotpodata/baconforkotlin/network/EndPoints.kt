package com.hotpodata.baconforkotlin.network

import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.Thing
import retrofit.http.GET
import retrofit.http.Header
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

/**
 * Created by jdrotos on 11/29/15.
 */
interface EndPoints {
    @GET("/r/{subreddit}/hot/.json")
    fun getSubReddit(@Path("subreddit") subreddit: String, @Query("after") after: String?): Observable<Thing>

    @GET("/r/{subreddit}/random/.json")
    fun getRandomSubredditPost(@Path("subreddit") subreddit: String, @Query("cachedistraction") depth: String?): Observable<MutableList<Thing>>

    @GET("/r/{subreddit}/comments/{article}/.json")
    fun getArticleComments(@Path("subreddit") subreddit: String, @Path("article") articleId: String, @Query("depth") depth: Int?, @Query("limit") limit: Int?): Observable<MutableList<Thing>>

    @GET("/api/v1/authorize?client_id=CLIENT_ID&response_type=TYPE&%20state=RANDOM_STRING&redirect_uri=URI&duration=DURATION&scope=SCOPE_STRING")
    fun authorize(@Query("client_id") clientId: String, @Query("response_type") responseType: String, @Query("state") state: String, @Query("redirect_uri") redirectUri: String, @Query("duration") duration: Long, @Query("scope") scope: String)
}