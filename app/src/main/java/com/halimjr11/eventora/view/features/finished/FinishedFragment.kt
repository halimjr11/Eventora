package com.halimjr11.eventora.view.features.finished

import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.eventora.databinding.FragmentFinishedBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.VerticalEventAdapter
import com.halimjr11.eventora.view.features.finished.viewmodel.FinishedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FinishedFragment :
    BaseFragment<FragmentFinishedBinding, FinishedViewModel>(FragmentFinishedBinding::inflate) {
    override val viewModel: FinishedViewModel by viewModel()
    private val verticalEventAdapter: VerticalEventAdapter by lazy {
        VerticalEventAdapter()
    }

    override fun setupUI() = with(binding) {
        progressFinished.run {
            isIndeterminate = true
            show()
        }

        rvFinished.apply {
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
        launchAndCollect(pastEvents) { state ->
            when (state) {
                is UiState.Success -> verticalEventAdapter.submitList(state.data)
                is UiState.Error -> {
                    binding.evFinished.setMessageAndCallback(state.message) {
                        loadPastEvents()
                    }
                }

                else -> {}
            }
            binding.run {
                loadingFinished.visibleIf(state is UiState.Loading)
                rvFinished.visibleIf(state is UiState.Success)
                evFinished.visibleIf(state is UiState.Error)
            }
        }
        super.observeData()
    }
}