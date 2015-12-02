package com.hotpodata.baconmasher.data

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray
import timber.log.Timber
import java.util.*

/**
 * Created by jdrotos on 11/30/15.
 */
class ActiveStringManager(var ctx: Context, val PREF_KEY: String, val defaultGen: ActiveStringManager.DefaultGenerator) {

    public abstract class DefaultGenerator() {
        abstract fun getAllDefault(): List<String>
        abstract fun getActiveDefault(): List<String>
    }

    val PREF_ALL = "ALL"
    val PREF_ACTIVE = "ACTIVE"

    var context: Context
    var all: MutableList<String>
    var active: MutableSet<String>

    var random: Random


    init {
        context = ctx.applicationContext
        random = Random()
        all = ArrayList<String>(readAllFromStorage())
        active = HashSet<String>(readActiveFromStorage())
        if (all.size <= 0) {
            resetToDefaults()
        }
    }

    public fun activeAsList(): List<String> {
        var activeList = ArrayList<String>(active)
        Collections.sort(activeList)
        return activeList
    }

    public fun getRandomActive() : String{
        var activeList = activeAsList()
        return activeList.get(random.nextInt(activeList.size))
    }

    public fun isActive(str: String): Boolean {
        return active.contains(str)
    }

    public fun setActive(str: String, isActive: Boolean) {
        if(isActive) {
            active.add(str)
        }else{
            active.remove(str)
        }
        writeActiveToStorage()
    }

    public fun setAllActive(allActive: Boolean) {
        if (allActive) {
            active = HashSet<String>(all)
        } else {
            active = HashSet<String>()
        }
        writeActiveToStorage()
    }

    public fun add(str: String, isActive: Boolean) {
        if (!all.contains(str)) {
            all.add(str)
            Collections.sort(all)
            writeAllToStorage()
        }
        if (isActive) {
            active.add(str)
            writeActiveToStorage()
        }
    }

    public fun remove(str: String) {
        if (all.contains(str)) {
            all.remove(str)
            writeAllToStorage()
        }
        if (active.contains(str)) {
            active.remove(str)
            writeActiveToStorage()
        }
    }

    private fun readAllFromStorage(): List<String> {
        var sharedPref = getSharedPrefs()
        var allPref = sharedPref.getStringSet(PREF_ALL, HashSet<String>())
        return ArrayList<String>(allPref)
    }

    private fun readActiveFromStorage(): List<String> {
        var sharedPref = getSharedPrefs()
        var activePref = sharedPref.getStringSet(PREF_ACTIVE, HashSet<String>())
        return ArrayList<String>(activePref)
    }

    private fun writeAllToStorage() {
        var sharedPref = getSharedPrefs()
        with(sharedPref.edit()) {
            putStringSet(PREF_ALL, HashSet<String>(all))
            commit()
        }
    }

    private fun writeActiveToStorage() {
        var sharedPref = getSharedPrefs()
        with(sharedPref.edit()) {
            putStringSet(PREF_ACTIVE, HashSet<String>(active))
            commit()
        }
    }

    public fun resetToDefaults() {
        all = ArrayList<String>(defaultGen.getAllDefault())
        Collections.sort(all)
        writeAllToStorage()
        active = HashSet<String>(defaultGen.getActiveDefault())
        writeActiveToStorage()
    }

    private fun getSharedPrefs(): SharedPreferences {
        return context!!.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);
    }
}