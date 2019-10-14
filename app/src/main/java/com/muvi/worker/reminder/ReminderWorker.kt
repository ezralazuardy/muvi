/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.worker.reminder

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.muvi.R
import com.muvi.config.AppConfig
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoUnit
import java.net.SocketException
import java.util.concurrent.TimeUnit

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), AnkoLogger {

    companion object {

        const val notificationId = 101
        const val notificationGroupId = "NotificationGroupReminderWorker"
        const val notificationChannelId = "NotificationReminderWorker"
        const val notificationChannelName = "ReminderWorker"
        var time: String? = null

        fun start(context: Context, time: String) {
            this.time = time
            var timeSplit = time.split(":")
            timeSplit = if(timeSplit.size == 2) timeSplit else AppConfig.DEFAULT_REMINDER_TIME.split(":")
            val alarmTime = LocalTime.of(timeSplit[0].toInt(), timeSplit[1].toInt())
            var now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES)
            val nowTime = now.toLocalTime()
            if (nowTime == alarmTime || nowTime.isAfter(alarmTime)) now = now.plusDays(1)
            now = now.withHour(alarmTime.hour).withMinute(alarmTime.minute)
            val duration = Duration.between(LocalDateTime.now(), now)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
                .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                AppConfig.WORKER_REMINDER_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }

        fun restart(context: Context, time: String) {
            stop(context)
            start(context, time)
        }

        fun stop(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(AppConfig.WORKER_REMINDER_NAME)
        }
    }

    override suspend fun doWork(): Result = coroutineScope {
        val context = applicationContext
        var isScheduleNext = true
        try {
            showNotification(context)
            Result.success()
        } catch (e: Exception) {
            error { e.message }
            if (runAttemptCount > 3) return@coroutineScope Result.success()
            when(e.cause) {
                is SocketException -> {
                    isScheduleNext = false
                    Result.retry()
                } else -> {
                    Result.failure()
                }
            }
        } finally {
            delay(1000)
            if(isScheduleNext) time?.let { start(context, it) }
        }
    }

    private fun showNotification(context: Context) {
        val intent = Intent(context, ListActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        context.getSystemService(Context.NOTIFICATION_SERVICE)?.apply {
            this as NotificationManager
            NotificationCompat.Builder(context, notificationChannelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_reminder_title))
                .setContentText(context.getString(R.string.notification_reminder_description))
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(notificationGroupId)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()
                .let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        this.apply {
                            createNotificationChannel(
                                NotificationChannel(
                                    notificationChannelId,
                                    notificationChannelName,
                                        NotificationManager.IMPORTANCE_HIGH
                                )
                            )
                        }
                    }
                    notify(notificationId, it)
                }
        }
    }
}