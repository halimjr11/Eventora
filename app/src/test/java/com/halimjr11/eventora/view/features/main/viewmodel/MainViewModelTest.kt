package com.halimjr11.eventora.view.features.main.viewmodel

import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.utils.MainDispatcherRule
import com.halimjr11.eventora.view.features.settings.worker.EventReminderWorker
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    private lateinit var viewModel: MainViewModel
    private val workManager: WorkManager = mockk(relaxed = true)
    private val eventRemoteRepository: EventRemoteRepository = mockk(relaxed = true)
    private val eventLocalRepository: EventLocalRepository = mockk(relaxed = true)
    private val dummyEvent = EventDomain(
        name = "Hackathon 2025",
        ownerName = "Tech Community",
        beginTime = "2025-10-21T10:00:00"
    )

    @Before
    fun setup() {
        viewModel = MainViewModel(workManager, eventLocalRepository, eventRemoteRepository)
    }

    @Test
    fun `toggleNotification true should enqueue work`() = runTest {
        every { eventLocalRepository.isNotificationEnabled() } returns true
        coEvery { eventRemoteRepository.getNearestEvent() } returns dummyEvent

        viewModel.toggleNotification(true)
        advanceUntilIdle()

        coVerify { eventRemoteRepository.getNearestEvent() }
        verify { workManager.enqueue(any<PeriodicWorkRequest>()) }
    }

    @Test
    fun `toggleNotification false should cancel work`() = runTest {
        // When
        viewModel.toggleNotification(false)

        // Then
        verify { workManager.cancelUniqueWork(any()) }
    }

    @Test
    fun `scheduleDailyReminder builds Data with event info`() = runTest {
        // Given
        every { eventLocalRepository.isNotificationEnabled() } returns true
        coEvery { eventRemoteRepository.getNearestEvent() } returns dummyEvent

        // When
        viewModel.scheduleDailyReminder()
        advanceUntilIdle()

        // Then
        coVerify { eventRemoteRepository.getNearestEvent() }
        verify {
            workManager.enqueue(
                withArg<WorkRequest> { workRequest ->
                    val data = workRequest.workSpec.input
                    assertEquals(true, data.getBoolean(EventReminderWorker.NOTIF_ENABLE_KEY, false))
                    assertEquals(
                        "Hackathon 2025",
                        data.getString(EventReminderWorker.NOTIF_NAME_KEY)
                    )
                    assertEquals(
                        "Tech Community",
                        data.getString(EventReminderWorker.NOTIF_AUTHOR_KEY)
                    )
                }
            )
        }
    }

    @Test
    fun `calculateInitialDelay should return non-negative minutes`() {
        val delay = viewModel.invokePrivateCalculateDelay()
        assertTrue(delay >= 0)
    }

    // Helper untuk akses private function pakai reflection
    private fun MainViewModel.invokePrivateCalculateDelay(): Long {
        val method = MainViewModel::class.java.getDeclaredMethod("calculateInitialDelay")
        method.isAccessible = true
        return method.invoke(this) as Long
    }

}