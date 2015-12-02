package com.hotpodata.baconmasher.utils

import java.util.regex.Pattern

/**
 * Created by jdrotos on 12/2/15.
 */
object ImgurUtils {
    val IMGUR_URL_PATTERN = "^http(s)?://(((m)\\.)|((www)\\.)|((i)\\.))?imgur.com/(a/)?[a-zA-Z0-9&]+((\\.jpg)|(\\.gif)|(\\.png))?".toRegex()
    val IMGUR_ALBUM_URL_PATTERN = "^https?://(?:m\\.)?(?:www\\.)?imgur\\.com/a/([a-zA-Z0-9]+)".toRegex()
    val IMGUR_GALLERY_URL_PATTERN = "^https?://(?:www\\.)?imgur\\.com/gallery/([a-zA-Z0-9]+)".toRegex()
    val IMGUR_IMAGE_PATTERN = "^https?://(www\\.)?(i\\.)?(m\\.)?imgur\\.com/.{3,7}\\.((jpg)|(gif)|(png))".toRegex()
    val IMGUR_HASH_PATTERN = "imgur\\.com/(([a-zA-Z0-9]{5,7}[&,]?)+)".toRegex()

    public fun isImgurUrl(url: String): Boolean {
        return IMGUR_URL_PATTERN.matches(url)
    }

    public fun isImgurAlbumUrl(url: String): Boolean {
        return IMGUR_ALBUM_URL_PATTERN.matches(url)
    }

    public fun isImgurGallertyUrl(url: String): Boolean {
        return IMGUR_GALLERY_URL_PATTERN.matches(url)
    }

    public fun isImgurDirectLinkUrl(url: String): Boolean {
        return IMGUR_IMAGE_PATTERN.matches(url)
    }

    public fun isImgurHashLinkUrl(url: String) : Boolean{
        return url.contains(IMGUR_HASH_PATTERN)
    }
}