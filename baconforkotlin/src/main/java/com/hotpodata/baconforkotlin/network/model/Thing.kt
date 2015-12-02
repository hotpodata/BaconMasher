package com.hotpodata.baconforkotlin.network.model

/**
 * Created by jdrotos on 11/29/15.
 */
open class Thing {

    //    String	id	this item's identifier, e.g. "8xwlg"
    //    String	name	Fullname of comment, e.g. "t1_c3v7f8u"
    //    String	kind	All things have a kind. The kind is a String identifier that denotes the object's type. Some examples: Listing, more, t1, t2
    //    Object	data	A custom data structure used to hold valuable information. This object's format will follow the data structure respective of its kind. See below for specific structures.

    var id: String? = null
    var name: String? = null
    var kind: String? = null
    var data: ThingData? = null
}