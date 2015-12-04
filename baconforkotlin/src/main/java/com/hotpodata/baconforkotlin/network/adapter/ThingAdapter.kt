package com.hotpodata.baconforkotlin.network.adapter

import com.github.salomonbrys.kotson.fromJson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.hotpodata.baconforkotlin.GsonHelper
import com.hotpodata.baconforkotlin.network.model.Listing
import com.hotpodata.baconforkotlin.network.model.Thing
import com.hotpodata.baconforkotlin.network.model.t1
import com.hotpodata.baconforkotlin.network.model.t3
import timber.log.Timber

/**
 * Created by jdrotos on 11/29/15.
 */
class ThingAdapter : TypeAdapter<Thing>() {

    override fun read(jsonReader: JsonReader?): Thing? {
        if (jsonReader != null) {
            if (jsonReader.peek() != JsonToken.BEGIN_OBJECT) {
                jsonReader.skipValue()
            } else {
                var thing: Thing = Thing()
                jsonReader.beginObject()
                while (jsonReader.hasNext()) {
                    when (jsonReader.nextName()) {
                        "id" -> {
                            if (jsonReader.peek() != JsonToken.NULL) {
                                thing.id = jsonReader.nextString()
                            } else {
                                jsonReader.skipValue()
                            }
                        }
                        "name" -> {
                            if (jsonReader.peek() != JsonToken.NULL) {
                                thing.name = jsonReader.nextString()
                            } else {
                                jsonReader.skipValue()
                            }
                        }
                        "kind" -> {
                            if (jsonReader.peek() != JsonToken.NULL) {
                                thing.kind = jsonReader.nextString()
                            } else {
                                jsonReader.skipValue()
                            }
                        }
                        "data" -> {
                            if (thing.kind != null && jsonReader.peek() != JsonToken.NULL) {
                                var kind = thing.kind
                                when (kind) {
                                    "Listing" -> {
                                        thing.data = readListing(jsonReader)
                                    }
                                    "t1" -> {
                                        thing.data = readT1(jsonReader)
                                    }
                                    "t3" -> {
                                        thing.data = readT3(jsonReader)
                                    }
                                    else -> {
                                        jsonReader.skipValue()
                                    }
                                }
                            } else {
                                jsonReader.skipValue()
                            }
                        }
                        else -> {
                            jsonReader.skipValue()
                        }
                    }
                }
                jsonReader.endObject()
                return thing
            }
        }
        return null
    }

    fun readListing(jsonReader: JsonReader): Listing? {
        Timber.d("readListing")
        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            return GsonHelper.gson.fromJson<Listing>(jsonReader)
        } else {
            jsonReader.skipValue()
        }
        return null
    }

    fun readT1(jsonReader: JsonReader): t1? {
        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            return GsonHelper.gson.fromJson<t1>(jsonReader)
        } else {
            jsonReader.skipValue()
        }
        return null
    }

    fun readT3(jsonReader: JsonReader): t3? {
        if (jsonReader.peek() == JsonToken.BEGIN_OBJECT) {
            return GsonHelper.gson.fromJson<t3>(jsonReader)
        } else {
            jsonReader.skipValue()
        }
        return null
    }


    override fun write(out: JsonWriter?, value: Thing?) {
        throw UnsupportedOperationException()
    }
}