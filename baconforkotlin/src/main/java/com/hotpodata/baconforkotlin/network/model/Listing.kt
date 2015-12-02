package com.hotpodata.baconforkotlin.network.model

import java.util.*

/**
 * Created by jdrotos on 11/29/15.
 */
open class Listing : ThingData {
    var before: String? = null
    var after: String? = null
    var modhash: String? = null
    var children: ArrayList<Thing>? = null
}