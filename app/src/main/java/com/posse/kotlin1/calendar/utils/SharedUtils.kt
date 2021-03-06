package com.posse.kotlin1.calendar.utils

import android.content.SharedPreferences

var SharedPreferences.themeSwitch: Boolean
    get() = this.getBoolean("themeSwitch", true)
    set(value) {
        this.edit()
            .putBoolean("themeSwitch", value)
            .apply()
    }

var SharedPreferences.lightTheme: Boolean
    get() = this.getBoolean("lightTheme", true)
    set(value) {
        this.edit()
            .putBoolean("lightTheme", value)
            .apply()
    }

var SharedPreferences.statsUsed: Boolean
    get() = this.getBoolean("statsUsed", false)
    set(value) {
        this.edit()
            .putBoolean("statsUsed", value)
            .apply()
    }

var SharedPreferences.nickName: String?
    get() = this.getString("nickName", null)
    set(value) {
        this.edit()
            .putString("nickName", value)
            .apply()
    }

var SharedPreferences.token: String?
    get() = this.getString("token", null)
    set(value) {
        this.edit()
            .putString("token", value)
            .apply()
    }

var SharedPreferences.locale: String?
    get() = this.getString("locale", null)
    set(value) {
        this.edit()
            .putString("locale", value)
            .apply()
    }