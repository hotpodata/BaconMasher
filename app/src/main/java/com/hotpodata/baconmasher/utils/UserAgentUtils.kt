package com.hotpodata.baconmasher.utils

import android.content.Context
import android.content.pm.PackageManager
import com.hotpodata.baconmasher.R
import timber.log.Timber

/**
 * Created by jdrotos on 12/4/15.
 */
object UserAgentUtils {
    public fun genUserAgentStr(ctx: Context, redditUserName: String) :String{
        var version: String = "?"
        try {
            val pInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
            version = ctx.resources.getString(R.string.version_template, pInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "Version fail")
        }
        //"android:com.hotpodata.baconmasher.free:v1.0.0 (by /u/hotpodata)"
        return "android:" + ctx.packageName + ":v" + version + " (by /u/" + redditUserName + ")";
    }
}