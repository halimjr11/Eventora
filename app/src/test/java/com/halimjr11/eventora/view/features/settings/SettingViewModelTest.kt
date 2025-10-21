package com.halimjr11.eventora.view.features.settings

import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.ui.helper.ThemeManager
import com.halimjr11.eventora.utils.AppTheme
import com.halimjr11.eventora.view.features.settings.viewmodel.SettingViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test


class SettingViewModelTest {
    private lateinit var viewModel: SettingViewModel
    private val themeManager: ThemeManager = mockk(relaxed = true)
    private val eventLocalRepository: EventLocalRepository = mockk(relaxed = true)

    @Before
    fun setup() {
        viewModel = SettingViewModel(themeManager, eventLocalRepository)
    }

    @Test
    fun `initial state matches system dark mode setting`() = runTest {
        every { themeManager.isSystemDarkMode() } returns true

        viewModel.toggleDarkMode(true)

        val initial = viewModel.isDarkModeEnabled.value
        assertTrue(initial)
    }

    @Test
    fun `initial state matches system light mode setting`() = runTest {
        every { themeManager.isSystemDarkMode() } returns false

        viewModel.toggleDarkMode(false)

        val initial = viewModel.isDarkModeEnabled.value
        assertFalse(initial)
    }

    @Test
    fun `toggleDarkMode updates state and applies theme`() = runTest {
        every { themeManager.isSystemDarkMode() } returns false

        viewModel.toggleDarkMode(true)

        val state = viewModel.isDarkModeEnabled.value
        assertTrue(state)

        verify { themeManager.applyTheme(AppTheme.DARK) }
    }

    @Test
    fun `toggleDarkMode false updates state and applies theme`() = runTest {
        every { themeManager.isSystemDarkMode() } returns true

        viewModel.toggleDarkMode(false)

        val state = viewModel.isDarkModeEnabled.first()
        assertFalse(state)

        verify { themeManager.applyTheme(AppTheme.LIGHT) }
    }

    @Test
    fun `toggleNotification should enable notification`() = runTest {
        coEvery { eventLocalRepository.setNotificationEnabled(true) } returns Unit

        viewModel.toggleNotification(true)

        coVerify(exactly = 1) { eventLocalRepository.setNotificationEnabled(true) }
        assertTrue(viewModel.isNotificationEnabled.value)
    }

    @Test
    fun `toggleNotification should disable notification`() = runTest {
        coEvery { eventLocalRepository.setNotificationEnabled(false) } returns Unit

        viewModel.toggleNotification(false)

        coVerify(exactly = 1) { eventLocalRepository.setNotificationEnabled(false) }
        assertFalse(viewModel.isNotificationEnabled.value)
    }

}