package com.halimjr11.eventora.view.features.home

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.carousel.CarouselSnapHelper
import com.halimjr11.eventora.databinding.FragmentHomeBinding
import com.halimjr11.eventora.ui.base.BaseFragment
import com.halimjr11.eventora.ui.helper.goToDetail
import com.halimjr11.eventora.ui.helper.launchAndCollect
import com.halimjr11.eventora.ui.helper.visibleIf
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.adapters.CarouselAdapter
import com.halimjr11.eventora.view.adapters.MoreEventAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<FragmentHomeBinding, HomeViewModel>(FragmentHomeBinding::inflate) {
    override val viewModel: HomeViewModel by viewModel()
    private val moreEventAdapter: MoreEventAdapter by lazy {
        MoreEventAdapter()
    }
    private val carouselAdapter: CarouselAdapter by lazy {
        CarouselAdapter()
    }

    override fun setupUI() = with(binding) {
        nsvContent.isNestedScrollingEnabled = false
        progressCircular.run {
            isIndeterminate = true
            show()
        }
        rvUpcoming.apply {
            adapter = moreEventAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        recyclerCarousel.apply {
            adapter = carouselAdapter
            val snapHelper = CarouselSnapHelper()
            snapHelper.attachToRecyclerView(this)
        }
        super.setupUI()
    }

    override fun setupListeners() = with(binding) {
        moreEventAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }
        carouselAdapter.setOnClickCallback { event ->
            context?.goToDetail(event.id)
        }
        super.setupListeners()
    }

    override fun observeData() = with(viewModel) {
        launchAndCollect(upcomingEvents) { state ->
            binding.run {
                loadingUpcoming.visibleIf(state is UiState.Loading)
                nsvContent.visibleIf(state is UiState.Success)
                evUpcoming.visibleIf(state is UiState.Error)
            }
            when (state) {
                is UiState.Success -> {
                    val (carousel, moreEvents) = state.data
                    carouselAdapter.submitList(carousel)
                    moreEventAdapter.submitList(moreEvents)
                    startAutoSlide()
                }

                is UiState.Error -> {
                    binding.evUpcoming.setMessageAndCallback(state.message) {
                        loadUpcomingEvents()
                    }
                }

                else -> {}
            }
        }
        super.observeData()
    }

    private fun startAutoSlide() {
        var currentPosition = 0
        viewLifecycleOwner.lifecycleScope.launch {
            while (isActive) {
                delay(3000)
                if (carouselAdapter.itemCount == 0) break

                currentPosition = (currentPosition + 1) % carouselAdapter.itemCount
                binding.recyclerCarousel.smoothScrollToPosition(currentPosition)
            }
        }
    }
}