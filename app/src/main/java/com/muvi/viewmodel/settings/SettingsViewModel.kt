/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.viewmodel.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.muvi.config.AppConfig
import com.muvi.repository.SettingsRepository
import com.muvi.repository.utils.UtilsRepository

class SettingsViewModel(
    application: Application,
    private val settingsRepository: SettingsRepository,
    private val utilsRepository: UtilsRepository
) : AndroidViewModel(application) {

    var reminderStatus: Boolean = false
        set(status) {
            settingsRepository.reminderStatus = status.also {
                if(status) settingsRepository.startReminder() else settingsRepository.stopReminder()
            }
            field = status
        }
        get() = settingsRepository.reminderStatus

    var reminderTime: String = AppConfig.DEFAULT_REMINDER_TIME
        set(time) {
            settingsRepository.reminderTime = time
            if(reminderStatus) settingsRepository.restartReminder()
            field = time
        }
        get() = settingsRepository.reminderTime

    var newReleaseStatus: Boolean = false
        set(status) {
            settingsRepository.newReleaseStatus = status.also {
                if(status) settingsRepository.startNewRelease() else settingsRepository.stopNewRelease()
            }
            field = status
        }
        get() = settingsRepository.newReleaseStatus

    fun getAppLanguage(): String = settingsRepository.getAppLanguage()

    fun getFormattedTime(time: String) =
        utilsRepository.formatTime(AppConfig.DEFAULT_TIME_FORMAT_12, time)
}