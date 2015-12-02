package com.hotpodata.baconforkotlin.network

import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.Thing
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query
import rx.Observable

/**
 * Created by jdrotos on 11/29/15.
 */
interface  EndPoints {
    @GET("/r/{subreddit}/hot/.json")
    fun getSubReddit(@Path("subreddit") subreddit: String, @Query("after") after: String?): Observable<Thing>

    @GET("/r/{subreddit}/comments/{article}/.json")
    fun getArticleComments(@Path("subreddit") subreddit: String, @Path("article") articleId: String): Observable<MutableList<Thing>>
}