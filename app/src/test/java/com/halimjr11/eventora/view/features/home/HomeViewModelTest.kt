package com.halimjr11.eventora.view.features.home

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.usecase.GetUpcomingUseCase
import com.halimjr11.eventora.utils.Constants.DATA_ERROR
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.MainDispatcherRule
import com.halimjr11.eventora.utils.UiState
import com.halimjr11.eventora.view.features.home.viewmodel.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: HomeViewModel
    private val getUpcomingUseCase: GetUpcomingUseCase = mockk(relaxed = true)
    private val dummyPairEvents = Pair(
        first = listOf(EventDomain(id = 1, name = "Sample Event 1")),
        second = listOf(EventDomain(id = 2, name = "Sample Event 2"))
    )

    @Before
    fun setup() {
        viewModel = HomeViewModel(getUpcomingUseCase, object : CoroutineDispatcherProvider {
            override val io = mainDispatcherRule.testDispatcher
            override val default = mainDispatcherRule.testDispatcher
            override val unconfined = mainDispatcherRule.testDispatcher
            override val main = mainDispatcherRule.testDispatcher
        })
    }

    @Test
    fun `loadUpcomingEvents emits Success`() = runTest {
        coEvery { getUpcomingUseCase() } returns DomainResult.Success(dummyPairEvents)

        viewModel.loadHomeEvents()
        advanceUntilIdle()

        val state = viewModel.homeEvents.value
        assert(state is UiState.Success && state.data == dummyPairEvents)
    }

    @Test
    fun `loadUpcomingEvents emits Error when failure`() = runTest {
        coEvery { getUpcomingUseCase() } returns DomainResult.Error

        viewModel.loadHomeEvents()
        advanceUntilIdle()

        val state = viewModel.homeEvents.value
        assert(state is UiState.Error && state.message == DATA_ERROR)
    }
}