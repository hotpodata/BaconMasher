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
    }
}