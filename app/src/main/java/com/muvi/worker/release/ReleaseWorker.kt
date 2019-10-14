/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.worker.release

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.muvi.BuildConfig
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.config.ContentType
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.repository.MovieRepository
import com.muvi.view.detail.DetailActivity
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.net.SocketException
import java.util.concurrent.TimeUnit

class ReleaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent, AnkoLogger {

    companion object {

        const val notificationId = 102
        const val notificationGroupId = "NotificationGroupReleaseWorker"
        const val notificationChannelId = "NotificationReleaseWorker"
        const val notificationChannelName = "ReleaseWorker"
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
            val workRequest = OneTimeWorkRequestBuilder<ReleaseWorker>()
                .setInitialDelay(duration.seconds, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                AppConfig.WORKER_REMINDER_NAME,
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }

        fun stop(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(AppConfig.WORKER_REMINDER_NAME)
        }
    }

    private val movieRepository: MovieRepository by inject()

    override suspend fun doWork(): Result = coroutineScope {
        val context = applicationContext
        var isScheduleNext = true
        try {
            showData(context)
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

    private suspend fun showData(context: Context) {
        val discoverMovieList = movieRepository.getRelease(
            BuildConfig.TMDB_API_KEY,
            AppConfig.TMDB_API_DEFAULT_LANGUAGE,
            DateTimeFormatter.ofPattern(AppConfig.TMDB_DATE_FORMAT).format(LocalDateTime.now().toLocalDate()),
            DateTimeFormatter.ofPattern(AppConfig.TMDB_DATE_FORMAT).format(LocalDateTime.now().toLocalDate())
        ).results.filter { !it.poster_path.isNullOrEmpty() }
        showNotification(context, discoverMovieList[0])
    }

    private fun showNotification(context: Context, movie: DiscoverMovieListResult) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(AppConfig.INTENT_EXTRA_CONTENT_TYPE, ContentType.MOVIE)
        intent.putExtra(AppConfig.INTENT_EXTRA_DATA_MOVIE, movie)
        intent.putExtra(AppConfig.INTENT_EXTRA_OPEN_FROM, this::class.java.simpleName)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(notificationId, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        context.getSystemService(Context.NOTIFICATION_SERVICE)?.apply {
            this as NotificationManager
            NotificationCompat.Builder(context, notificationChannelId)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_new_release_title, movie.title))
                .setContentText(movie.overview)
                .setDefaults(Notification.DEFAULT_ALL)
                .setGroup(notificationGroupId)
                .setAutoCancel(true)
                .setStyle(NotificationCompat.BigPictureStyle()
                    .bigPicture(
                        Glide.with(context)
                            .asBitmap()
                            .load(AppConfig.TMDB_API_IMAGE_BASE_URL + movie.poster_path)
                            .apply(RequestOptions().transform(CenterCrop()))
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .placeholder(R.drawable.background_poster_item_list)
                            .error(R.drawable.background_poster_item_list)
                            .submit()
                            .get()
                    )
                    .setBigContentTitle(context.getString(R.string.notification_new_release_title, movie.title))
                )
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