/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.muvi.config.AppConfig
import com.muvi.worker.release.ReleaseWorker
import com.muvi.worker.reminder.ReminderWorker
import org.jetbrains.anko.defaultSharedPreferences
import java.util.*

class SettingsRepository(private val context: Context) {

    private val sharedPreferences: SharedPreferences? by lazy {
        context.defaultSharedPreferences
    }

    var appLanguage: String = Locale.getDefault().displayLanguage

    var reminderStatus: Boolean = false
        set(status) {
            sharedPreferences?.edit {
                putBoolean(AppConfig.SHARED_PREFERENCE_APP_SETTINGS_REMAINDER, status)
            }
            field = status
        }
        get() {
            return sharedPreferences?.getBoolean(
                AppConfig.SHARED_PREFERENCE_APP_SETTINGS_REMAINDER,
                false
            ) ?: false
        }

    var reminderTime: String = AppConfig.DEFAULT_REMINDER_TIME
        set(time) {
            sharedPreferences?.edit {
                putString(AppConfig.SHARED_PREFERENCE_APP_SETTINGS_REMAINDER_TIME, time)
            }
            field = time
        }
        get() {
            return sharedPreferences?.getString(
                AppConfig.SHARED_PREFERENCE_APP_SETTINGS_REMAINDER_TIME,
                AppConfig.DEFAULT_REMINDER_TIME
            ) ?: AppConfig.DEFAULT_REMINDER_TIME
        }

    var newReleaseStatus: Boolean = false
        set(status) {
            sharedPreferences?.edit {
                putBoolean(AppConfig.SHARED_PREFERENCE_APP_SETTINGS_NEW_RELEASE, status)
            }
            field = status
        }
        get() {
            return sharedPreferences?.getBoolean(
                AppConfig.SHARED_PREFERENCE_APP_SETTINGS_NEW_RELEASE,
                false
            ) ?: false
        }

    fun startReminder() = ReminderWorker.start(context, reminderTime)

    fun restartReminder() = ReminderWorker.restart(context, reminderTime)

    fun stopReminder() = ReminderWorker.stop(context)

    fun startNewRelease() = ReleaseWorker.start(context, AppConfig.DEFAULT_NEW_RELEASE_TIME)

    fun stopNewRelease() = ReleaseWorker.stop(context)
}