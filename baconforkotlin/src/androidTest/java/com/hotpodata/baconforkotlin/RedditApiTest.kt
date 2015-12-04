package com.hotpodata.baconforkotlin

import android.test.AndroidTestCase
import android.util.Base64
import com.google.gson.GsonBuilder
import com.hotpodata.baconforkotlin.network.RedditApi
import com.hotpodata.baconforkotlin.network.adapter.ThingAdapter
import com.hotpodata.baconforkotlin.network.model.*
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber
import java.util.*
import java.util.concurrent.Semaphore

/**
 * Created by jdrotos on 11/29/15.
 */
class RedditApiTest : AndroidTestCase() {

    override fun setUp() {
        super.setUp()
        Timber.plant(Timber.DebugTree())
    }

    public fun testGetAppToken() {
        Timber.plant(Timber.DebugTree())
        var bacon = Bacon("https://ssl.reddit.com", "android:com.hotpodata.baconmasher.test:v1.0.0 (by /u/hotpodata)")
        var basicAuth = "Basic " + Base64.encodeToString("v56Ugwjl6WUqMA:".toByteArray(), Base64.NO_WRAP);

        var sem = Semaphore(1)
        sem.acquire()

        var tokenResp: AccessTokenResponse? = null
        var error: Throwable? = null

//        bacon.service
//                //.accessToken(basicAuth, "https://oauth.reddit.com/grants/installed_client", "http://www.hotpodata.com/baconmasher/auth/redirect_uri", UUID.randomUUID().toString())
//                .accessToken(basicAuth, "https://oauth.reddit.com/grants/installed_client", "http://www.hotpodata.com/baconmasher/auth/redirect_uri", UUID.randomUUID().toString())
//                .subscribeOn(Schedulers.io())
//                .observeOn(Schedulers.io())//We observe here because the main thread will be blocking on a semaphore.
//                .subscribe(
//                        {
//                            tokenResp = it
//                            sem.release()
//                        },
//                        {
//                            error = it
//                            sem.release()
//                        })
//
//        sem.acquire()
//        Timber.d(" sem.acquire()")
//        if (tokenResp != null) {
//            Timber.d("tokenResp - token:" + tokenResp?.access_token + " expires_in:" + tokenResp?.expires_in)
//        }
//        if (error != null) {
//            Timber.e(error, "Bad news cuz")
//        }
//        assertNotNull(tokenResp)
//        assertNull(error)

    }



    //    public fun testSubredditFetch() {
    //        var bacon = Bacon("http://www.reddit.com/","Android:com.hotpodata.test")
    //        var service = bacon.service
    //        var thing = service.getSubReddit("earthporn").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toBlocking().first()
    //        assertNotNull(thing)
    //        assertEquals("Listing", thing.kind)
    //        assert(thing.data is Listing)
    //        var data = thing.data
    //        if (data is Listing) {
    //            var children = data.children
    //            assertNotNull(children)
    //            assert(children?.size ?: 0 > 0)
    //            for(child in children!!){
    //                assertNotNull(child.data)
    //                if(child.kind == "t1"){
    //                    assert(child.data is t1)
    //                }
    //                if(child.kind == "t3"){
    //                    assert(child.data is t3)
    //                }
    //                if(child.kind == "Listing"){
    //                    assert(child.data is Listing)
    //                }
    //            }
    //        }
    //    }


}