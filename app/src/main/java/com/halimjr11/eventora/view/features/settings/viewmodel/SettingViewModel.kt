package com.halimjr11.eventora.view.features.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.utils.AppTheme
import com.halimjr11.eventora.utils.toAppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingViewModel(
    private val themeManager: ThemeManager,
    private val localRepository: EventLocalRepository
) : ViewModel() {
    private val _isDarkModeEnabled = MutableStateFlow(
        value = localRepository.getThemeKey()?.let {
            it == AppTheme.DARK.name
        } ?: themeManager.isSystemDarkMode()
    )
    val isDarkModeEnabled: StateFlow<Boolean> = _isDarkModeEnabled

    private val _isNotificationEnabled = MutableStateFlow(localRepository.isNotificationEnabled())
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled


    fun toggleDarkMode(enabled: Boolean) {
        _isDarkModeEnabled.value = enabled
        val themeKey = if (enabled) AppTheme.DARK.name else AppTheme.LIGHT.name
        localRepository.setThemeKey(themeKey)
        themeManager.applyTheme(themeKey.toAppTheme())
    }

    fun toggleNotification(isEnabled: Boolean) {
        localRepository.setNotificationEnabled(isEnabled)
        _isNotificationEnabled.value = isEnabled
    }

}