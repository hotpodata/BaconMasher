package com.hotpodata.baconforkotlin

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hotpodata.baconforkotlin.network.adapter.ThingAdapter
import com.hotpodata.baconforkotlin.network.model.Thing

/**
 * Created by jdrotos on 12/4/15.
 */
object GsonHelper {
    private var _gson: Gson? = null
    public var gson: Gson
        get() {
            if (_gson == null) {
                _gson = genFreshGson()
            }
            return _gson!!
        }
        set(value) {
            _gson = value
        }

    public fun genFreshGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Thing::class.java, ThingAdapter())
                .create()
    }
}