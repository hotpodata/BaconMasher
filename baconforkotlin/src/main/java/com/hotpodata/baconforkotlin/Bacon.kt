package com.hotpodata.baconforkotlin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hotpodata.baconforkotlin.network.EndPoints
import com.hotpodata.baconforkotlin.network.adapter.ThingAdapter
import com.hotpodata.baconforkotlin.network.model.Thing
import com.squareup.okhttp.CacheControl
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Response
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import timber.log.Timber

/**
 * Created by jdrotos on 11/29/15.
 */
class Bacon(val endpoint: String, val useragent: String) {

    //val ENDPOINT = "https://www.reddit.com"



    private var _endpoints: EndPoints? = null
    var service: EndPoints
        get() {
            if (_endpoints == null) {
                _endpoints = genService()
            }
            return _endpoints!!
        }
        set(value) {
            _endpoints = value
        }

    object GsonHelper{
        private var _gson: Gson? = null
        public var gson: Gson
            get() {
                if (_gson == null) {
                    _gson = GsonHelper.genGson()
                }
                return _gson!!
            }
            set(value) {
                _gson = value
            }

        public fun genGson(): Gson {
            return GsonBuilder()
                    .registerTypeAdapter(Thing::class.java, ThingAdapter())
                    .create()
        }
    }


    private fun genService(): EndPoints {
        var gson = GsonHelper.genGson()

        var logger = HttpLoggingInterceptor({
            Timber.d(it)
        })
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)

        var client = OkHttpClient()

        //Set up logging
        client.interceptors().add(logger)

        //Add the user agent
        client.interceptors().add(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                var req = chain?.request()?.let {
                    var builder = it.newBuilder()
                    builder.header("User-Agent", useragent).build()
                }
                return chain?.proceed(req)
            }
        })

        //Turn off cacheing
        client.interceptors().add(object: Interceptor{
            override fun intercept(chain: Interceptor.Chain?): Response? {
                var req = chain?.request()?.let {
                    with(it.newBuilder()) {
                        //cacheControl(CacheControl.FORCE_NETWORK)
                        header("Cache-Control","no-cache")
                        build()
                    }
                }
                return chain?.proceed(req)
            }

        })

        var retrofit = retrofit.Retrofit.Builder()
                .baseUrl(endpoint)
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