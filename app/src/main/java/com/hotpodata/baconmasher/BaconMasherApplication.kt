package com.hotpodata.baconmasher

import android.support.multidex.MultiDexApplication
import com.hotpodata.baconmasher.BuildConfig
import timber.log.Timber

/**
 * Created by jdrotos on 9/17/15.
 */
class BaconMasherApplication : MultiDexApplication() {


    public override fun onCreate(){
        super.onCreate()
        if(BuildConfig.LOGGING_ENABLED) {
            Timber.plant(Timber.DebugTree())
        }
        MashMaster.initMashMaster(this)
    }



}