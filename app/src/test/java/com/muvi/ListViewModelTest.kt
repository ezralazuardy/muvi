package com.muvi

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.list.ListViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ListViewModelTest {

    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var movieRepository: MovieRepository

    @Mock
    lateinit var tvRepository: TvRepository

    @Mock
    lateinit var movieDiscoverObserver: Observer<List<DiscoverMovieListResult>>

    @Mock
    lateinit var tvDiscoverObserver: Observer<List<DiscoverTvListResult>>

    @Mock
    lateinit var genreObserver: Observer<List<Genre>>

    private lateinit var listViewModel: ListViewModel

    @Before
    fun setUp() {
        listViewModel = ListViewModel(application, movieRepository, tvRepository)
    }

    @Test
    fun getLoaded() {
        assertEquals(false, listViewModel.loaded)
    }

    @Test
    fun setLoaded() {
        listViewModel.loaded = true
        assertEquals(true, listViewModel.loaded)
    }

    @Test
    fun getDiscoverMovies() {
        listViewModel.discoverMovies.observeForever(movieDiscoverObserver)
//        listViewModel.discoverMovies.observeForever {
//            verify(it).isNotEmpty()
//            assertEquals(20, it.size)
//            println(it.toString())
//        }
    }

    @Test
    fun getDiscoverTvs() {
        listViewModel.discoverTvs.observeForever {
            verify(it).isNotEmpty()
            assertEquals(20, it.size)
        }
    }

    @Test
    fun getMovieGenres() {
        listViewModel.movieGenres.observeForever {
            verify(it).isNotEmpty()
        }
    }

    @Test
    fun getTvGenres() {
        listViewModel.tvGenres.observeForever {
            verify(it).isNotEmpty()
        }
    }
}