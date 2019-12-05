package com.muvi.viewmodel.favourite

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muvi.factory.MovieFactory
import com.muvi.factory.TvFactory
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.rule.CoroutineTestRule
import com.muvi.utils.TestUtils.getOrAwaitValue
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
class FavouriteViewModelTest {

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

    private lateinit var favouriteViewModel: FavouriteViewModel

    @Before
    fun setUp() {
        favouriteViewModel = FavouriteViewModel(application, movieRepository, tvRepository)
    }

    @Test
    fun getLoaded() = runBlockingTest {
        false.run {
            assertEquals(this, favouriteViewModel.loaded)
            print("getLoaded(): $this")
        }
    }

    @Test
    fun setLoaded() = runBlockingTest {
        true.run {
            favouriteViewModel.loaded = this
            assertEquals(this, favouriteViewModel.loaded)
            print("setLoaded(): $this")
        }
    }

    @Test
    fun getFavouriteMovies() = runBlockingTest {
        MovieFactory.createDummyMovieEntities().let {
            `when`(movieRepository.getFavouriteList()).thenReturn(it)
            favouriteViewModel.getFavouriteMovies()
            with(favouriteViewModel.favouriteMovies.getOrAwaitValue()) {
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
            favouriteViewModel.getFavouriteTvs()
            with(favouriteViewModel.favouriteTvs.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getFavouriteTvs(): $this")
            }
        }
    }
}