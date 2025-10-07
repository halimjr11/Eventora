package com.halimjr11.eventora.view.features.detail

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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: DetailViewModel
    private val eventRepository: EventRepository = mockk(relaxed = true)
    private val dummyEvent = EventDomain(id = 1, name = "Sample Event")

    @Before
    fun setup() {
        viewModel = DetailViewModel(eventRepository, object : CoroutineDispatcherProvider {
            override val io = mainDispatcherRule.testDispatcher
            override val default = mainDispatcherRule.testDispatcher
            override val unconfined = mainDispatcherRule.testDispatcher
            override val main = mainDispatcherRule.testDispatcher
        })
    }

    @Test
    fun `setId sets idDetail and loads detail successfully`() = runTest {
        coEvery { eventRepository.getEventDetail(any()) } returns DomainResult.Success(dummyEvent)

        viewModel.setId(dummyEvent.id)
        advanceUntilIdle()

        assert(1 == viewModel.idDetail)
        assertTrue(viewModel.detailEvent.value is UiState.Success)
        val state = viewModel.detailEvent.value as UiState.Success
        assert(state.data == dummyEvent)
    }

    @Test
    fun `loadDetailEvent sets state to Error on failure`() = runTest {
        coEvery { eventRepository.getEventDetail(any()) } returns DomainResult.Error

        viewModel.setId(2)
        advanceUntilIdle()

        assert(2 == viewModel.idDetail)
        assertTrue(viewModel.detailEvent.value is UiState.Error)
        val state = viewModel.detailEvent.value as UiState.Error
        assert(UNKNOWN_ERROR == state.message)
    }
}