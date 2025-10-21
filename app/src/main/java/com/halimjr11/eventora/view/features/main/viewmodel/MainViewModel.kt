package com.halimjr11.eventora.view.features.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.halimjr11.eventora.domain.model.EventDomain
import com.halimjr11.eventora.domain.repository.EventLocalRepository
import com.halimjr11.eventora.domain.repository.EventRemoteRepository
import com.halimjr11.eventora.utils.Constants.TARGET_HOUR_NOTIF
import com.halimjr11.eventora.utils.Constants.TARGET_MINUTE_NOTIF
import com.halimjr11.eventora.utils.Constants.UNIQUE_WORK_NAME
import com.halimjr11.eventora.view.features.settings.worker.EventReminderWorker
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class MainViewModel(
    private val workManager: WorkManager,
    private val localRepository: EventLocalRepository,
    private val remoteRepository: EventRemoteRepository
) : ViewModel() {
    private var event: EventDomain? = null

    fun toggleNotification(isEnabled: Boolean) {
        if (isEnabled) scheduleDailyReminder()
        else cancelReminder()
    }

    fun scheduleDailyReminder() = viewModelScope.launch {
        val initialDelayMinutes = calculateInitialDelay()
        if (event == null) {
            event = remoteRepository.getNearestEvent()
        }
        val data = Data.Builder()
            .putBoolean(
                EventReminderWorker.NOTIF_ENABLE_KEY,
                localRepository.isNotificationEnabled()
            )
            .putString(EventReminderWorker.NOTIF_NAME_KEY, event?.name.orEmpty())
            .putString(EventReminderWorker.NOTIF_AUTHOR_KEY, event?.ownerName.orEmpty())
            .putString(EventReminderWorker.NOTIF_BEGIN_TIME_KEY, event?.beginTime.orEmpty())
            .build()
        val workRequest =
            PeriodicWorkRequest.Builder(EventReminderWorker::class.java, 1, TimeUnit.DAYS)
                .setInitialDelay(initialDelayMinutes, TimeUnit.MINUTES)
                .setInputData(data)
                .build()

        workManager.enqueue(workRequest)
    }

    private fun cancelReminder() {
        workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
    }

    private fun calculateInitialDelay(): Long {
        val now = LocalDateTime.now()
        var targetTime = now.withHour(TARGET_HOUR_NOTIF)
            .withMinute(TARGET_MINUTE_NOTIF)
            .withSecond(0)
            .withNano(0)

        if (targetTime.isBefore(now)) {
            targetTime = targetTime.plusDays(1)
        }

        val duration = Duration.between(now, targetTime)
        return duration.toMinutes()
    }
}