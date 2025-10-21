package com.halimjr11.eventora.view.features.detail

import com.halimjr11.eventora.core.coroutines.CoroutineDispatcherProvider
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.utils.Constants.UNKNOWN_ERROR
import com.halimjr11.eventora.utils.DomainResult
import com.halimjr11.eventora.utils.MainDispatcherRule
import com.halimjr11.eventora.utils.UiState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: DetailViewModel
    private val eventRemoteRepository: EventRemoteRepository = mockk(relaxed = true)
    private val eventLocalRepository: EventLocalRepository = mockk(relaxed = true)
    private val dummyEvent = EventDomain(id = 1, name = "Sample Event")

    @Before
    fun setup() {
        viewModel = DetailViewModel(
            eventRemoteRepository,
            eventLocalRepository,
            object : CoroutineDispatcherProvider {
                override val io = mainDispatcherRule.testDispatcher
                override val default = mainDispatcherRule.testDispatcher
                override val unconfined = mainDispatcherRule.testDispatcher
                override val main = mainDispatcherRule.testDispatcher
            })
    }

    @Test
    fun `setId sets idDetail and loads detail successfully`() = runTest {
        coEvery { eventRemoteRepository.getEventDetail(any()) } returns DomainResult.Success(
            dummyEvent
        )

        viewModel.setId(dummyEvent.id)
        advanceUntilIdle()

        assert(1 == viewModel.idDetail)
        assertTrue(viewModel.detailEvent.value is UiState.Success)
        val state = viewModel.detailEvent.value as UiState.Success
        assert(state.data == dummyEvent)
    }

    @Test
    fun `loadDetailEvent sets state to Error on failure`() = runTest {
        coEvery { eventRemoteRepository.getEventDetail(any()) } returns DomainResult.Error

        viewModel.setId(2)
        advanceUntilIdle()

        assert(2 == viewModel.idDetail)
        assertTrue(viewModel.detailEvent.value is UiState.Error)
        val state = viewModel.detailEvent.value as UiState.Error
        assert(UNKNOWN_ERROR == state.message)
    }

    @Test
    fun `toggleFavorite should insert when not favorite`() = runTest {
        // Given
        coEvery { eventLocalRepository.insertEvent(any()) } returns Unit

        // When
        viewModel.toggleFavorite(dummyEvent)
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { eventLocalRepository.insertEvent(dummyEvent) }
        assertTrue(viewModel.isFavorite.value)
    }

    @Test
    fun `toggleFavorite should delete when already favorite`() = runTest {
        coEvery { eventLocalRepository.insertEvent(any()) } returns Unit
        coEvery { eventLocalRepository.deleteEvent(any()) } returns Unit

        viewModel.toggleFavorite(dummyEvent)
        advanceUntilIdle()
        assertTrue(viewModel.isFavorite.value)

        viewModel.toggleFavorite(dummyEvent)
        advanceUntilIdle()

        coVerify(exactly = 1) { eventLocalRepository.deleteEvent(dummyEvent) }
        assertFalse(viewModel.isFavorite.value)
    }
}