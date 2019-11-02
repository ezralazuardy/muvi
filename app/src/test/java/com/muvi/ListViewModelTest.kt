package com.muvi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.muvi.factory.MovieFactory
import com.muvi.factory.TvFactory
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.list.ListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var tvRepository: TvRepository

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")
    private lateinit var listViewModel: ListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        listViewModel = ListViewModel(application, movieRepository, tvRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun getLoaded() {
        assertEquals(false, listViewModel.loaded)
        println("getLoaded(): ${listViewModel.loaded}")
    }

    @Test
    fun setLoaded() {
        listViewModel.loaded = true
        assertEquals(true, listViewModel.loaded)
        println("setLoaded(): ${listViewModel.loaded}")
    }

    @Test
    fun getDiscoverMovies() {
        `when`(movieRepository.getDummyDiscoverList()).thenReturn(MovieFactory.createDummyMovies())
        with(listViewModel.dummyDiscoverMovies.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(3, this.size)
            println("getDiscoverMovies(): $this")
        }
    }

    @Test
    fun getDiscoverTvs() {
        `when`(tvRepository.getDummyDiscoverList()).thenReturn(TvFactory.createDummyTvs())
        with(listViewModel.dummyDiscoverTvs.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(3, this.size)
            println("getDiscoverTvs(): $this")
        }
    }

    @Test
    fun getMovieGenres() {
        `when`(movieRepository.getDummyGenres()).thenReturn(MovieFactory.createDummyGenres())
        with(listViewModel.dummyMovieGenres.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(5, this.size)
            println("getMovieGenres(): $this")
        }
    }

    @Test
    fun getTvGenres() {
        `when`(tvRepository.getDummyGenres()).thenReturn(TvFactory.createDummyGenres())
        with(listViewModel.dummyTvGenres.getOrAwaitValue()) {
            assertNotNull(this)
            assertEquals(5, this.size)
            println("getTvGenres(): $this")
        }
    }

    /* Copyright 2019 Google LLC.
     * SPDX-License-Identifier: Apache-2.0 */
    fun <T> LiveData<T>.getOrAwaitValue(time: Long = 2, timeUnit: TimeUnit = TimeUnit.SECONDS): T {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                this@getOrAwaitValue.removeObserver(this)
            }
        }
        this.observeForever(observer)
        if (!latch.await(time, timeUnit)) throw TimeoutException("LiveData value was never set.")
        @Suppress("UNCHECKED_CAST")
        return data as T
    }
}