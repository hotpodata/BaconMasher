package com.hotpodata.baconforkotlin

import android.util.Base64
import com.hotpodata.baconforkotlin.network.RedditApi
import com.squareup.okhttp.Interceptor
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Response
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import rx.Observable
import rx.lang.kotlin.BehaviourSubject
import timber.log.Timber
import java.net.URLDecoder

/**
 * Created by jdrotos on 12/4/15.
 */
class RedditSessionService(val userAgent: String, val uniqueDeviceId: String, val applicationId: String) {
    val AuthEndpoint = "https://ssl.reddit.com"
    val ContentEndpoint = "https://oauth.reddit.com"

    var authenticatedRedditApi: RedditApi? = null
    var authToken: String? = null
    var authExpires: Long = 0

    public fun getAuthenticatedService(): Observable<RedditApi> {
        if (authenticatedRedditApi != null) {
            if (authToken != null && authExpires != null && authExpires > (System.currentTimeMillis() / 1000L)) {
                return Observable.just(authenticatedRedditApi)
            }
        }
        return Observable
                .just(genService(AuthEndpoint, dressApplicationIdForAuthHeader(applicationId)))
                .flatMap {
                    it.accessToken("https://oauth.reddit.com/grants/installed_client", uniqueDeviceId)
                }
                .map {
                    var token = it.access_token
                    if (token != null) {
                        authToken = token
                        authExpires = (System.currentTimeMillis() / 1000L) + it.expires_in
                        token
                    } else {
                        throw RuntimeException("Empty token")
                    }
                }
                .map {
                    var api = genService(ContentEndpoint, dressTokenForAuthHeader(it))
                    authenticatedRedditApi = api
                    api
                }
    }

    private fun dressApplicationIdForAuthHeader(appId: String): String {
        return "Basic " + Base64.encodeToString("v56Ugwjl6WUqMA:".toByteArray(), Base64.NO_WRAP);
    }

    private fun dressTokenForAuthHeader(token: String): String {
        return "Bearer " + token
    }

    private fun genService(endpoint: String, authHeader: String): RedditApi {
        var gson = GsonHelper.gson
        var client = OkHttpClient()

        //Add the user agent
        client.interceptors().add(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                var req = chain?.request()?.let {
                    it.newBuilder().header("User-Agent", userAgent).build()
                }
                Timber.d("New req User-Agent:" + req?.header("User-Agent"))
                return chain?.proceed(req)
            }
        })

        //Add auth
        client.interceptors().add(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                var req = chain?.request()?.let {
                    it.newBuilder().header("Authorization", authHeader).build()
                }
                Timber.d("New req Authorization:" + req?.header("Authorization"))
                return chain?.proceed(req)
            }
        })

        //Sanatize headers - sometimes reddit redirect headers contained a full url in
        //the "location" header. This url would sometimes contain special characters. e.g. umlouts
        //which would cause okhttp to throw an exception. No More!
        client.networkInterceptors().add(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain?): Response? {
                var req = chain?.request()
                return chain?.proceed(req)?.let {
                    var location = it.header("location")?.let {
                        var loc = URLDecoder.decode(it, "UTF-8")
                        Timber.d("location header fix - oig:" + it + " new:" + loc)
                        loc
                    }
                    if (location != null) {
                        it.newBuilder().header("location", location).build()
                    } else {
                        it
                    }
                }
            }
        })

        var logger = HttpLoggingInterceptor({
            Timber.d(it)
        })
        logger.setLevel(HttpLoggingInterceptor.Level.BODY)
        client.interceptors().add(logger)

        var retrofit = retrofit.Retrofit.Builder()
                .baseUrl(endpoint)
                .client(client)
                .addCallAdapterFactory(retrofit.RxJavaCallAdapterFactory.create())
                .addConverterFactory(retrofit.GsonConverterFactory.create(gson))
                .build();

        var service = retrofit.create(RedditApi::class.java)
        return service
    }


}