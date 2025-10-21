package com.halimjr11.eventora.data.preferences.impl

import android.content.SharedPreferences
import androidx.core.content.edit
import com.halimjr11.eventora.data.preferences.SharedPreferenceHelper
import com.halimjr11.eventora.utils.Constants.NOTIFICATION_KEY
import com.halimjr11.eventora.utils.Constants.THEME_KEY

class SharedPreferenceHelperImpl(
    private val sharedPreferences: SharedPreferences
) : SharedPreferenceHelper {
    override fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(NOTIFICATION_KEY, false)
    }

    override fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit(commit = true) { putBoolean(NOTIFICATION_KEY, enabled) }
    }

    override fun getThemeKey(): String? {
        return sharedPreferences.getString(THEME_KEY, null)
    }

    override fun setThemeKey(key: String) {
        sharedPreferences.edit(commit = true) { putString(THEME_KEY, key) }
    }
}