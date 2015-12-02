package com.hotpodata.baconforkotlin

import android.test.AndroidTestCase
import com.google.gson.GsonBuilder
import com.hotpodata.baconforkotlin.network.EndPoints
import com.hotpodata.baconforkotlin.network.adapter.ThingAdapter
import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.Thing
import com.hotpodata.baconforkotlin.network.model.t1
import com.hotpodata.baconforkotlin.network.model.t3
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.logging.HttpLoggingInterceptor
import retrofit.GsonConverterFactory
import retrofit.Retrofit
import retrofit.RxJavaCallAdapterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by jdrotos on 11/29/15.
 */
class RedditApiTest : AndroidTestCase() {

    override fun setUp() {
        super.setUp()
        Timber.plant(Timber.DebugTree())
    }

    public fun testSubredditFetch() {
        var service = Bacon.service
        var thing = service.getSubReddit("earthporn").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).toBlocking().first()
        assertNotNull(thing)
        assertEquals("Listing", thing.kind)
        assert(thing.data is Listing)
        var data = thing.data
        if (data is Listing) {
            var children = data.children
            assertNotNull(children)
            assert(children?.size ?: 0 > 0)
            for(child in children!!){
                assertNotNull(child.data)
                if(child.kind == "t1"){
                    assert(child.data is t1)
                }
                if(child.kind == "t3"){
                    assert(child.data is t3)
                }
                if(child.kind == "Listing"){
                    assert(child.data is Listing)
                }
            }
        }
    }


}