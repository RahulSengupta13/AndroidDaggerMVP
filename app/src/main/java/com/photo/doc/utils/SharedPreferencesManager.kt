package com.photo.doc.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {
    private var sharedPreferences: SharedPreferences? = null
    private var sharedPreferencesEditor: SharedPreferences.Editor? = null

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        sharedPreferencesEditor = sharedPreferences?.edit()
    }

    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        sharedPreferencesEditor?.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        sharedPreferencesEditor?.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return sharedPreferences?.getBoolean(IS_FIRST_TIME_LAUNCH, true) ?: true
    }

    companion object {
        var PRIVATE_MODE = 0
        private const val PREF_NAME = "intro_slider-welcome"
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }
}