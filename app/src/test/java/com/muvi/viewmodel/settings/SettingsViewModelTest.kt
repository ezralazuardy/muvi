package com.muvi.viewmodel.settings

import android.app.Application
import com.muvi.config.AppConfig
import com.muvi.repository.SettingsRepository
import com.muvi.repository.utils.UtilsRepository
import com.muvi.utils.Utils
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SettingsViewModelTest {

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var settingsRepository: SettingsRepository

    @Mock
    lateinit var utilsRepository: UtilsRepository

    private lateinit var settingsViewModel: SettingsViewModel

    @Before
    fun setUp() {
        settingsViewModel = SettingsViewModel(application, settingsRepository, utilsRepository)
    }

    @Test
    fun getAppLanguage() {
        UUID.randomUUID().toString().let {
            `when`(settingsRepository.getAppLanguage()).thenReturn(it)
            with(settingsViewModel.getAppLanguage()) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getAppLanguage(): $this")
            }
        }
    }

    @Test
    fun getReminderStatus() {
        false.let {
            `when`(settingsRepository.reminderStatus).thenReturn(it)
            with(settingsViewModel.reminderStatus) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getReminderStatus(): $this")
            }
        }
    }

    @Test
    fun setReminderStatus() {
        true.let {
            `when`(settingsRepository.reminderStatus).thenReturn(it)
            settingsViewModel.reminderStatus = it
            with(settingsViewModel.reminderStatus) {
                assertNotNull(this)
                assertEquals(it, this)
                print("setReminderStatus(): $this")
            }
        }
    }

    @Test
    fun getReminderTime() {
        AppConfig.DEFAULT_REMINDER_TIME.let {
            `when`(settingsRepository.reminderTime).thenReturn(it)
            with(settingsViewModel.reminderTime) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getReminderTime(): $this")
            }
        }
    }

    @Test
    fun setReminderTime() {
        val hour = "${(0..23).random()}"
        val minute = "${(0..59).random()}"
        ("${if (hour.length < 2) "0$hour" else hour}:${if (minute.length < 2) "0$minute" else minute}").let {
            `when`(settingsRepository.reminderTime).thenReturn(it)
            settingsRepository.reminderTime = it
            with(settingsViewModel.reminderTime) {
                assertNotNull(this)
                assertEquals(it, this)
                print("setReminderTime(): $this")
            }
        }
    }

    @Test
    fun getNewReleaseStatus() {
        false.let {
            `when`(settingsRepository.newReleaseStatus).thenReturn(it)
            with(settingsViewModel.newReleaseStatus) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getNewReleaseStatus(): $this")
            }
        }
    }

    @Test
    fun setNewReleaseStatus() {
        true.let {
            `when`(settingsRepository.newReleaseStatus).thenReturn(it)
            settingsViewModel.newReleaseStatus = it
            with(settingsViewModel.newReleaseStatus) {
                assertNotNull(this)
                assertEquals(it, this)
                print("setNewReleaseStatus(): $this")
            }
        }
    }

    @Test
    fun getFormattedTime() {
        Utils.getDummyTime().let {
            UUID.randomUUID().toString().let { dummyString ->
                `when`(settingsViewModel.getFormattedTime(dummyString)).thenReturn(it)
                with(settingsViewModel.getFormattedTime(dummyString)) {
                    assertEquals(it, this)
                    print("getFormattedTime(): $this")
                }
            }
        }
    }
}