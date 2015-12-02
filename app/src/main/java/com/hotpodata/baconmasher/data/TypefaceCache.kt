package com.hotpodata.baconmasher.data

import android.content.Context
import android.graphics.Typeface
import java.util.*

/**
 * Created by jdrotos on 12/1/15.
 */
object TypefaceCache {
    val cache = HashMap<String, Typeface>()
    public fun getTypeFace(ctx: Context, typeface: String): Typeface {
        var tf = cache[typeface] ?: Typeface.createFromAsset(ctx.assets, "fonts/" + typeface);
        cache.put(typeface, tf)
        return tf
    }

}