package com.halimjr11.eventora.view.features.settings

import com.halimjr11.eventora.databinding.FragmentSettingBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.view.features.main.viewmodel.MainViewModel
import com.halimjr11.eventora.view.features.settings.viewmodel.SettingViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(FragmentSettingBinding::inflate) {
    override val viewModel: SettingViewModel by viewModel()
    private val mainViewModel: MainViewModel by activityViewModel()
    override fun setupListeners() = with(binding) {
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkMode(isChecked)
        }
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleNotification(isChecked)
            mainViewModel.toggleNotification(isChecked)
        }
        super.setupListeners()
    }

    override fun observeData() {
        launchAndCollect(viewModel.isDarkModeEnabled) { isEnabled ->
            binding.run {
                if (switchDarkMode.isChecked != isEnabled) {
                    switchDarkMode.isChecked = isEnabled
                }
            }
        }

        launchAndCollect(viewModel.isNotificationEnabled) { isEnabled ->
            if (isEnabled) mainViewModel.scheduleDailyReminder()
            binding.run {
                if (switchNotification.isChecked != isEnabled) {
                    switchNotification.isChecked = isEnabled
                }
            }
        }
        super.observeData()
    }
}