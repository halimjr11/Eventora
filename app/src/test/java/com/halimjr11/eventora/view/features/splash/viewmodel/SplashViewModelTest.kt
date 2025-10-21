package com.halimjr11.eventora.view.features.splash.viewmodel

import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.utils.AppTheme
import com.halimjr11.eventora.utils.MainDispatcherRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SplashViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: SplashViewModel
    private val themeManager: ThemeManager = mockk(relaxed = true)
    private val eventLocalRepository: EventLocalRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SplashViewModel(themeManager, eventLocalRepository)
    }

    @Test
    fun `checkAndApplyTheme should apply dark theme when DARK saved`() = runTest {
        // Arrange
        every { eventLocalRepository.getThemeKey() } returns "DARK"

        // Act
        viewModel.checkAndApplyTheme()

        // Assert
        verify { themeManager.applyTheme(AppTheme.DARK) }
    }

    @Test
    fun `checkAndApplyTheme should apply light theme when LIGHT saved`() = runTest {
        // Arrange
        every { eventLocalRepository.getThemeKey() } returns "LIGHT"

        // Act
        viewModel.checkAndApplyTheme()

        // Assert
        verify { themeManager.applyTheme(AppTheme.LIGHT) }
    }

    @Test
    fun `checkAndApplyTheme should apply light theme when SYSTEM saved`() = runTest {
        // Arrange
        every { eventLocalRepository.getThemeKey() } returns "SYSTEM"

        // Act
        viewModel.checkAndApplyTheme()

        // Assert
        verify { themeManager.applyTheme(AppTheme.SYSTEM) }
    }
}