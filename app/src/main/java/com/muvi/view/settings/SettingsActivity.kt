/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.settings

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.viewmodel.settings.SettingsViewModel
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setComponent()
    }

    private fun setComponent() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if(settingsViewModel.reminderStatus) switchReminder.isChecked = true
        textReminderValue.text = settingsViewModel.getFormattedTime(settingsViewModel.reminderTime)
        if(settingsViewModel.newReleaseStatus) switchNewRelease.isChecked = true
    }

    private fun showCurrentAppLanguage() {
        rowAppLanguageDescriptionActivitySettings.text = settingsViewModel.getAppLanguage()
    }

    private fun setReminder() {
        settingsViewModel.reminderStatus = true
        layoutActivitySettings.snackbar(getString(R.string.snackbar_set_reminder))
    }

    private fun unsetReminder() {
        settingsViewModel.reminderStatus = false
        layoutActivitySettings.snackbar(getString(R.string.snackbar_unset_reminder))
    }

    private fun setReminderTime(time: String, formattedTime: String) {
        settingsViewModel.reminderTime = time
        layoutActivitySettings.snackbar(
            getString(R.string.snackbar_set_reminder_time, formattedTime)
        )
    }

    private fun pickReminderTime() {
        val calendar = Calendar.getInstance()
        TimePickerDialog(
            this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                calendar.apply {
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                }
                val time = SimpleDateFormat(AppConfig.DEFAULT_TIME_FORMAT, Locale.getDefault()).format(calendar.time)
                val formattedTime = settingsViewModel.getFormattedTime(time)
                textReminderValue.text = formattedTime
                setReminderTime(time, formattedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun setNewReleaseNotification() {
        settingsViewModel.newReleaseStatus = true
        layoutActivitySettings.snackbar(getString(R.string.snackbar_set_new_release))
    }

    private fun unsetNewReleaseNotification() {
        settingsViewModel.newReleaseStatus = false
        layoutActivitySettings.snackbar(getString(R.string.snackbar_unset_new_release))
    }

    private fun setListener() {
        rowAppLanguageActivitySettings.onClick {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        rowAppReminderActivitySettings.onClick {
            switchReminder.isChecked = !switchReminder.isChecked
        }
        rowAppReminderTimeActivitySettings.onClick {
            pickReminderTime()
        }
        rowAppNotifyReleaseActivitySettings.onClick {
            switchNewRelease.isChecked = !switchNewRelease.isChecked
        }
        switchReminder.onCheckedChange { _, isChecked ->
            if(isChecked) setReminder() else unsetReminder()
        }
        switchNewRelease.onCheckedChange { _, isChecked ->
            if(isChecked) setNewReleaseNotification() else unsetNewReleaseNotification()
        }
    }

    private fun unsetListener() {
        rowAppLanguageActivitySettings.setOnClickListener(null)
        rowAppReminderActivitySettings.setOnClickListener(null)
        rowAppReminderTimeActivitySettings.setOnClickListener(null)
        rowAppNotifyReleaseActivitySettings.setOnClickListener(null)
        switchReminder.setOnClickListener(null)
        switchNewRelease.setOnClickListener(null)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        setListener()
        showCurrentAppLanguage()
    }

    override fun onStop() {
        super.onStop()
        unsetListener()
    }
}