package com.halimjr11.eventora.view.features.settings

import androidx.lifecycle.ViewModel
import com.halimjr11.eventora.ui.helper.ThemeManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingViewModel(
    private val themeManager: ThemeManager
) : ViewModel() {
    private val _isDarkModeEnabled = MutableStateFlow(themeManager.isSystemDarkMode())
    val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkModeEnabled.value = enabled
        themeManager.applyTheme(enabled)
    }
}