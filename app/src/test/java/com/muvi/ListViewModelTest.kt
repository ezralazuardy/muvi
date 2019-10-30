/*
 * Created by Ezra Lazuardy on 10/31/19, 12:23 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/31/19, 12:18 AM
 */

package com.muvi

import android.os.Build
import com.muvi.viewmodel.list.ListViewModel
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class ListViewModelTest : KoinTest {

    private val listViewModel: ListViewModel by inject()
    private val discoverMoviesTotal = 20
    private val discoverTvsTotal = 20

    @Test
    fun getDiscoverMovies() {
        listViewModel.discoverMovies.observeForever {
            assertNotNull(it)
            assertEquals(discoverMoviesTotal, it.size)
            verify(it).size
        }
    }

    @Test
    fun getDiscoverTvs() {
        listViewModel.discoverTvs.observeForever {
            assertNotNull(it)
            assertEquals(discoverTvsTotal, it.size)
            verify(it).size
        }
    }

    @After
    fun after() {
        stopKoin()
    }
}