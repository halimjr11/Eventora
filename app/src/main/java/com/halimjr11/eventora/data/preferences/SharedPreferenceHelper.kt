package com.halimjr11.eventora.data.preferences

interface SharedPreferenceHelper {
    fun isNotificationEnabled(): Boolean
    fun setNotificationEnabled(enabled: Boolean)

    fun getThemeKey(): String?
    fun setThemeKey(key: String)
}