package com.halimjr11.eventora.view.features.settings

import com.halimjr11.eventora.databinding.FragmentSettingBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.launchAndCollect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingFragment :
    BaseFragment<FragmentSettingBinding, SettingViewModel>(FragmentSettingBinding::inflate) {
    override val viewModel: SettingViewModel by viewModel()
    override fun setupListeners() = with(binding) {
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkMode(isChecked)
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
        super.observeData()
    }
}