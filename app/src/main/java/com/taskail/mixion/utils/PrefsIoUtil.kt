package com.taskail.mixion.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import com.taskail.mixion.appInstance

/**
 *Created by ed on 2/4/18.
 */


private fun edit(): SharedPreferences.Editor {
    return getPreferences().edit()
}

private fun getPreferences(): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(getContext())
}

private fun getResources(): Resources? {
    return getContext()?.resources
}

fun getContext(): Context? {
    return appInstance?.applicationContext
}

fun getString(key: String, defaultValue: String?): String? {
    return getPreferences().getString(key, defaultValue)
}

fun setString(key: String, value: String?) {
    edit().putString(key, value).apply()
}