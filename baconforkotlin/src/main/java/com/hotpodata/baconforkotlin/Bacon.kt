package com.hotpodata.baconforkotlin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hotpodata.baconforkotlin.network.EndPoints
import com.hotpodata.baconforkotlin.network.adapter.ThingAdapter
import com.hotpodata.baconforkotlin.network.model.Thing
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Created by jdrotos on 11/29/15.
 */
object Bacon {

    val ENDPOINT = "https://www.reddit.com"

    private var _gson: Gson? = null
    var gson: Gson
        get() {
            if(_gson == null){
                _gson = Bacon.genGson()
            }
            return _gson!!
        }
        set(value) {
            _gson = value
        }

    private var _endpoints: EndPoints? = null
    var service: EndPoints
        get() {
            if(_endpoints == null){
                _endpoints = Bacon.genService()
            }
            return _endpoints!!
        }
        set(value) {
            _endpoints = value
        }

    private fun genGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Thing::class.java, ThingAdapter())
                .create()
    }

    private fun genService(): EndPoints {
        var gson = genGson()

        var logger = HttpLoggingInterceptor({
            Timber.d(it)
        })
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = OkHttpClient()
        client.interceptors().add(logger)

        var retrofit = retrofit.Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .client(client)
                .addCallAdapterFactory(retrofit.RxJavaCallAdapterFactory.create())
                .addConverterFactory(retrofit.GsonConverterFactory.create(gson))
                .build();

        var service = retrofit.create(EndPoints::class.java)
        return service
    }


    //            var gson = GsonBuilder()
    //    .registerTypeAdapter(SubRedditData::class.java, SubRedditDataAdapter().nullSafe())
    //    .registerTypeAdapter(RedditArticleComments::class.java, RedditArticleCommentsAdapter())
    //    .create()
    //    var client = OkHttpClient()
    //    var logger = HttpLoggingInterceptor({
    //        Timber.d(it)
    //    })
    //    logger.setLevel(HttpLoggingInterceptor.Level.BODY)
    //    client.interceptors().add(logger)
    //
    //    var retrofit = retrofit.Retrofit.Builder()
    //            .baseUrl("https://www.reddit.com")
    //            .client(client)
    //            .addCallAdapterFactory(retrofit.RxJavaCallAdapterFactory.create())
    //            .addConverterFactory(retrofit.GsonConverterFactory.create(gson))
    //            .build();
    //
    //    var service = retrofit.create(RedditAPI::class.java)
}