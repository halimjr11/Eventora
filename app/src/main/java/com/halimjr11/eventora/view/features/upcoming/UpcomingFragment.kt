package com.halimjr11.eventora.view.features.upcoming

import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.eventora.databinding.FragmentUpcomingBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.VerticalEventAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpcomingFragment : BaseFragment<FragmentUpcomingBinding, UpcomingViewModel>(
    FragmentUpcomingBinding::inflate
) {
    override val viewModel: UpcomingViewModel by viewModel()
    private val verticalEventAdapter: VerticalEventAdapter by lazy {
        VerticalEventAdapter()
    }

    override fun setupUI() = with(binding) {
        progressUpcoming.run {
            isIndeterminate = true
            show()
        }

        rvUpcoming.apply {
            adapter = verticalEventAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        super.setupUI()
    }

    override fun setupListeners() {
        verticalEventAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(upcomingEvents) { state ->
            when (state) {
                is UiState.Success -> verticalEventAdapter.submitList(state.data)
                is UiState.Error -> {
                    binding.evUpcoming.setMessageAndCallback(state.message) {
                        loadUpcomingEvents()
                    }
                }

                else -> {}
            }
            binding.run {
                loadingUpcoming.visibleIf(state is UiState.Loading)
                rvUpcoming.visibleIf(state is UiState.Success)
                evUpcoming.visibleIf(state is UiState.Error)
            }
        }
        super.observeData()
    }

}