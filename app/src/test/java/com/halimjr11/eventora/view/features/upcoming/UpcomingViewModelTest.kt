package com.halimjr11.eventora.view.features.upcoming

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.data.repository.EventRepository
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.MainDispatcherRule
import com.halimjr11.eventora.utils.UiState
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpcomingViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: UpcomingViewModel
    private val eventRepository: EventRepository = mockk(relaxed = true)
    private val dummyEvents = listOf(EventDomain(id = 1, name = "Sample Event"))

    @Before
    fun setup() {
        viewModel = UpcomingViewModel(eventRepository, object : CoroutineDispatcherProvider {
            override val io = mainDispatcherRule.testDispatcher
            override val default = mainDispatcherRule.testDispatcher
            override val unconfined = mainDispatcherRule.testDispatcher
            override val main = mainDispatcherRule.testDispatcher
        })
    }

    @Test
    fun `loadUpcomingEvents emits Success`() = runTest {
        coEvery { eventRepository.getUpcomingEvents() } returns DomainResult.Success(dummyEvents)

        viewModel.loadUpcomingEvents()
        advanceUntilIdle()

        val state = viewModel.upcomingEvents.value
        assert(state is UiState.Success && state.data == dummyEvents)
    }

    @Test
    fun `loadUpcomingEvents emits Error when failure`() = runTest {
        coEvery { eventRepository.getUpcomingEvents() } returns DomainResult.Error

        viewModel.loadUpcomingEvents()
        advanceUntilIdle()

        val state = viewModel.upcomingEvents.value
        assert(state is UiState.Error && state.message == UNKNOWN_ERROR)
    }

}