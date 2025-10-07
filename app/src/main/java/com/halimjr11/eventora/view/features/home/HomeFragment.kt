package com.halimjr11.eventora.view.features.home

import androidx.recyclerview.widget.LinearLayoutManager
import com.halimjr11.eventora.databinding.FragmentHomeBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.FinishedAdapter
import com.halimjr11.eventora.view.adapters.UpcomingAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel: HomeViewModel by viewModel()
    private val finishedAdapter: FinishedAdapter by lazy {
        FinishedAdapter()
    }
    private val upcomingAdapter: UpcomingAdapter by lazy {
        UpcomingAdapter()
    }

    override fun setupUI() = with(binding) {
        progressCircular.run {
            isIndeterminate = true
            show()
        }
        progressFinished.run {
            isIndeterminate = true
            show()
        }
        rvUpcoming.apply {
            adapter = upcomingAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        rvFinished.apply {
            adapter = finishedAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        upcomingAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }

        finishedAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(upcomingEvents) { state ->
            binding.run {
                loadingUpcoming.visibleIf(state is UiState.Loading)
                rvUpcoming.visibleIf(state is UiState.Success)
                evUpcoming.visibleIf(state is UiState.Error)
            }
            when (state) {
                is UiState.Success -> {
                    upcomingAdapter.submitList(state.data)
                }

                is UiState.Error -> {
                    binding.evUpcoming.setMessageAndCallback(state.message) {
                        loadUpcomingEvents()
                    }
                }

                else -> {}
            }
        }
        launchAndCollect(pastEvents) { state ->
            when (state) {
                is UiState.Success -> finishedAdapter.submitList(state.data)
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