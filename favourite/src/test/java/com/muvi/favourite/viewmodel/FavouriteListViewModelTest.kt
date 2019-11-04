package com.muvi.favourite.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muvi.favourite.factory.MovieFactory
import com.muvi.favourite.factory.TvFactory
import com.muvi.favourite.repository.MovieRepository
import com.muvi.favourite.repository.TvRepository
import com.muvi.favourite.rule.CoroutineTestRule
import com.muvi.favourite.utils.Utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class FavouriteListViewModelTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var tvRepository: TvRepository

    private lateinit var favouriteListViewModel: FavouriteListViewModel

    @Before
    fun setUp() {
        favouriteListViewModel = FavouriteListViewModel(application, movieRepository, tvRepository)
    }

    @Test
    fun getLoaded() = runBlockingTest {
        false.run {
            assertEquals(this, favouriteListViewModel.loaded)
            print("getLoaded(): $this")
        }
    }

    @Test
    fun setLoaded() = runBlockingTest {
        true.run {
            favouriteListViewModel.loaded = this
            assertEquals(this, favouriteListViewModel.loaded)
            print("setLoaded(): $this")
        }
    }

    @Test
    fun getFavouriteMovies() = runBlockingTest {
        MovieFactory.createDummyMovieEntities().let {
            `when`(movieRepository.getFavouriteList()).thenReturn(it)
            favouriteListViewModel.getFavouriteMovies()
            with(favouriteListViewModel.favouriteMovies.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getFavouriteMovies(): $this")
            }
        }
    }

    @Test
    fun getFavouriteTvs() = runBlockingTest {
        TvFactory.createDummyTvEntities().let {
            `when`(tvRepository.getFavouriteList()).thenReturn(it)
            favouriteListViewModel.getFavouriteTvs()
            with(favouriteListViewModel.favouriteTvs.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getFavouriteTvs(): $this")
            }
        }
    }
}