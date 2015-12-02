package com.hotpodata.baconmasher.data

import org.json.JSONObject

/**
 * Created by jdrotos on 11/28/15.
 */
class MashData(val imageUrl: String, val comment: String, val font: String, val textGravity: String) {
    override fun equals(other: Any?): Boolean {
        if (other is MashData) {
            return hashCode() == other.hashCode()
        }
        return super.equals(other)
    }

    override fun hashCode(): Int {
        return imageUrl.hashCode() + comment.hashCode() + font.hashCode() + textGravity.hashCode()
    }

    object Serializer {
        val JSON_IMAGE_URL = "image"
        val JSON_COMMENT = "comment"
        val JSON_FONT = "font"
        val JSON_TEXT_GRAVITY = "textGravity"

        fun toJSON(mash: MashData): JSONObject {
            var json = JSONObject()
            json.put(JSON_IMAGE_URL, mash.imageUrl)
            json.put(JSON_COMMENT, mash.comment)
            json.put(JSON_FONT, mash.font)
            json.put(JSON_TEXT_GRAVITY, mash.textGravity)
            return json
        }

        fun fromJSON(json: JSONObject): MashData? {
            var image = json.optString(JSON_IMAGE_URL)
            var comment = json.optString(JSON_COMMENT)
            var font = json.optString(JSON_FONT)
            var textgrav = json.optString(JSON_TEXT_GRAVITY)
            if (image != null && comment != null && font != null) {
                return MashData(image, comment, font, textgrav)
            }
            return null
        }
    }
}