package com.muvi.viewmodel.search

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
import java.util.*

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SearchViewModelTest {

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

    private lateinit var searchViewModel: SearchViewModel

    @Before
    fun setUp() {
        searchViewModel = SearchViewModel(application, movieRepository, tvRepository)
    }

    @Test
    fun getLoaded() = runBlockingTest {
        false.run {
            assertEquals(this, searchViewModel.loaded)
            print("getLoaded(): $this")
        }
    }

    @Test
    fun setLoaded() = runBlockingTest {
        true.run {
            searchViewModel.loaded = this
            assertEquals(this, searchViewModel.loaded)
            print("setLoaded(): $this")
        }
    }

    @Test
    fun getSearchMovies() = runBlockingTest {
        MovieFactory.createDummyMovies().let {
            UUID.randomUUID().toString().let { dummyQuery ->
                `when`(
                    movieRepository
                        .search(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE, dummyQuery)
                )
                    .thenReturn(it)
                searchViewModel.searchMovies(query = dummyQuery)
                with(searchViewModel.searchMovies.getOrAwaitValue()) {
                    assertNotNull(this)
                    assertEquals(it, this)
                    assertEquals(it.size, this.size)
                    print("getSearchMovies(): $this")
                }
            }
        }
    }

    @Test
    fun getSearchTvs() = runBlockingTest {
        TvFactory.createDummyTvs().let {
            UUID.randomUUID().toString().let { dummyQuery ->
                `when`(
                    tvRepository
                        .search(BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE, dummyQuery)
                )
                    .thenReturn(it)
                searchViewModel.searchTvs(query = dummyQuery)
                with(searchViewModel.searchTvs.getOrAwaitValue()) {
                    assertNotNull(this)
                    assertEquals(it, this)
                    assertEquals(it.size, this.size)
                    print("getSearchTvs(): $this")
                }
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
            with(searchViewModel.movieGenres.getOrAwaitValue()) {
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
            with(searchViewModel.tvGenres.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                assertEquals(it.size, this.size)
                print("getTvGenres(): $this")
            }
        }
    }
}