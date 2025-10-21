package com.halimjr11.eventora.view.features.settings.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.halimjr11.eventora.R

class EventReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val isEnabled = inputData.getBoolean(NOTIF_ENABLE_KEY, true)
        val name = inputData.getString(NOTIF_NAME_KEY).orEmpty()
        val owner = inputData.getString(NOTIF_AUTHOR_KEY).orEmpty()
        val beginTime = inputData.getString(NOTIF_BEGIN_TIME_KEY).orEmpty()
        if (!isEnabled) return Result.retry()

        showNotification(name, owner, beginTime)
        return Result.success()
    }

    private fun showNotification(name: String, owner: String, beginTime: String) {
        val channelId = "event_reminder_channel"
        val channelName = "Event Reminder"
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_event)
            .setContentTitle(
                buildString {
                    append(applicationContext.resources.getString(R.string.upcoming_events))
                    append(" • ")
                    append(owner)
                }
            )
            .setContentText(
                buildString {
                    append(name)
                    append("\n")
                    append(beginTime)
                }
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(1, notification)
    }

    companion object {
        const val NOTIF_ENABLE_KEY = "enable_key"
        const val NOTIF_NAME_KEY = "name_key"
        const val NOTIF_AUTHOR_KEY = "author_key"
        const val NOTIF_BEGIN_TIME_KEY = "begin_time_key"
    }

}
