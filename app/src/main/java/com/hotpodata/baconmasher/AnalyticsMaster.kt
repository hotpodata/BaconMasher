package com.hotpodata.baconmasher

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker

/**
 * Created by jdrotos on 11/18/15.
 */
object AnalyticsMaster {

    //SCREENS
    val SCREEN_MASHER = "Masher"

    //CATEGORIES
    val CATEGORY_ACTION = "Action"

    //ACTIONS
    val ACTION_MASH = "Mash"
    val ACTION_SHARE = "Share"
    val ACTION_ADD_COMMENT_REDDIT = "AddCommentReddit"
    val ACTION_ADD_IMAGE_REDDIT = "AddImageReddit"
    val ACTION_REMOVE_COMMENT_REDDIT = "RemoveCommentReddit"
    val ACTION_REMOVE_IMAGE_REDDIT = "RemoveImageReddit"
    val ACTION_SET_IMAGE_SUBREDDIT_ACTIVE = "ActivateImageReddit"
    val ACTION_SET_IMAGE_SUBREDDIT_INACTIVE = "DeactivateImageReddit"
    val ACTION_SET_COMMENT_SUBREDDIT_ACTIVE = "ActivateCommentReddit"
    val ACTION_SET_COMMENT_SUBREDDIT_INACTIVE = "DeactivateCommentReddit"
    val ACTION_SET_FONT_ACTIVE = "ActivateFont"
    val ACTION_SET_FONT_INACTIVE = "DeactivateFont"
    val ACTION_ERROR_SHOWN = "ErrorShown"

    val ACTION_RATE_APP = "Rate_App"
    val ACTION_FILECAT = "FileCat"
    val ACTION_REDCHAIN = "RedChain"
    val ACTION_WIKICAT = "WikiCat"
    val ACTION_CONTACT = "Contact"
    val ACTION_WEBSITE = "Website"
    val ACTION_TWITTER = "Twitter"
    val ACTION_GITHUB = "GitHub"
    val ACTION_GO_PRO_SIDEBAR = "Go_Pro_Sidebar"

    //Labels (note, sometimes labels are generated in code. e.g. subreddit name)
    val LABEL_MASH_COUNT = "MashCount"

    private var tracker: Tracker? = null
    public fun getTracker(context: Context): Tracker {
        val t = tracker ?:
                GoogleAnalytics.getInstance(context).newTracker(R.xml.global_tracker).apply {
                    enableExceptionReporting(true)
                    enableAdvertisingIdCollection(true)
                    enableAutoActivityTracking(true)
                }
        tracker = t
        return t
    }
}