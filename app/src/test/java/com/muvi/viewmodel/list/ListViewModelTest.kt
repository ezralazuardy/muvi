package com.muvi.viewmodel.list

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.factory.MovieFactory
import com.muvi.factory.TvFactory
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.rule.CoroutineTestRule
import com.muvi.utils.Utils.getOrAwaitValue
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
class ListViewModelTest {

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

    private lateinit var listViewModel: ListViewModel

    @Before
    fun setUp() {
        listViewModel = ListViewModel(application, movieRepository, tvRepository)
    }

    @Test
    fun getLoaded() = runBlockingTest {
        false.run {
            assertEquals(this, listViewModel.loaded)
            print("getLoaded(): $this")
        }
    }

    @Test
    fun setLoaded() = runBlockingTest {
        true.run {
            listViewModel.loaded = this
            assertEquals(this, listViewModel.loaded)
            print("setLoaded(): $this")
        }
    }

    @Test
    fun getDiscoverMovies() = runBlockingTest {
        MovieFactory.createDummyMovies().let {
            `when`(
                movieRepository
                    .getDiscoverList(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            )
                .thenReturn(it)
            with(listViewModel.discoverMovies.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getDiscoverMovies(): $this")
            }
        }
    }

    @Test
    fun getDiscoverTvs() = runBlockingTest {
        TvFactory.createDummyTvs().let {
            `when`(
                tvRepository
                    .getDiscoverList(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            )
                .thenReturn(it)
            with(listViewModel.discoverTvs.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getDiscoverTvs(): $this")
            }
        }
    }

    @Test
    fun getMovieGenres() = runBlockingTest {
        MovieFactory.createDummyGenres().let {
            `when`(
                movieRepository
                    .getGenres(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            )
                .thenReturn(it)
            with(listViewModel.movieGenres.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getMovieGenres(): $this")
            }
        }
    }

    @Test
    fun getTvGenres() = runBlockingTest {
        TvFactory.createDummyGenres().let {
            `when`(
                tvRepository
                    .getGenres(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            )
                .thenReturn(it)
            with(listViewModel.tvGenres.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getTvGenres(): $this")
            }
        }
    }
}