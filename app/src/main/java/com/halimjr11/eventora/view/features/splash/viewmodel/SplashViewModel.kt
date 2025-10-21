package com.halimjr11.eventora.view.features.splash.viewmodel

import androidx.lifecycle.ViewModel
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.utils.AppTheme
import com.halimjr11.eventora.utils.toAppTheme

class SplashViewModel(
    private val themeManager: ThemeManager,
    private val localRepository: EventLocalRepository
) : ViewModel() {
    fun checkAndApplyTheme() {
        val appTheme: AppTheme = localRepository.getThemeKey().orEmpty().toAppTheme()
        themeManager.applyTheme(appTheme)
    }
}