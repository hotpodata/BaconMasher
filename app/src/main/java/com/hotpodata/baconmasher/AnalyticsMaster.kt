package com.hotpodata.baconmasher

import android.content.Context
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.hotpodata.baconmasher.R

/**
 * Created by jdrotos on 11/18/15.
 */
object AnalyticsMaster {

    //SCREENS
    val SCREEN_MASHER = "Masher"
    val SCREEN_PRO_FEATURES = "ProFeature"

    //CATEGORIES
    val CATEGORY_ACTION = "Action"

    //ACTIONS
    val ACTION_MASH = "Mash"
    val ACTION_SET_COMMENT_REDDIT = "SetCommentReddit"
    val ACTION_SET_IMAGE_REDDIT = "SetImageReddit"
    val ACTION_REMOVE_SETTINGS_REDDIT = "RemoveSettingsReddit"
    val ACTION_ADD_SETTINGS_REDDIT = "AddSettingsReddit"
    val ACTION_RATE_APP = "Rate_App"
    val ACTION_FILECAT = "FileCat"
    val ACTION_REDCHAIN = "RedChain"
    val ACTION_WIKICAT = "WikiCat"
    val ACTION_CONTACT = "Contact"
    val ACTION_WEBSITE = "Website"
    val ACTION_TWITTER = "Twitter"
    val ACTION_GITHUB = "GitHub"
    val ACTION_GO_PRO_SIDEBAR = "Go_Pro_Sidebar"
    val ACTION_GO_PRO_FRAGMENT = "Go_Pro_Fragment"
    val ACTION_TOGGLE_REMINDER_NOTIFICATION = "Toggle_Reminder_Notification"
    val ACTION_TOGGLE_BROKEN_NOTIFICATION = "Toggle_Broken_Chain_Notification"

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