package com.muvi.viewmodel.detail

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.factory.MovieFactory
import com.muvi.factory.TvFactory
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.repository.utils.UtilsRepository
import com.muvi.rule.CoroutineTestRule
import com.muvi.utils.TestUtils
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
import java.util.*

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

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

    @Mock
    lateinit var utilsRepository: UtilsRepository

    private lateinit var detailViewModel: DetailViewModel

    @Before
    fun setUp() {
        detailViewModel = DetailViewModel(application, movieRepository, tvRepository, utilsRepository)
    }

    @Test
    fun getLoaded() = runBlockingTest {
        false.run {
            assertEquals(this, detailViewModel.loaded)
            print("getLoaded(): $this")
        }
    }

    @Test
    fun setLoaded() = runBlockingTest {
        true.run {
            detailViewModel.loaded = this
            assertEquals(this, detailViewModel.loaded)
            print("setLoaded(): $this")
        }
    }

    @Test
    fun getMovieDetail() = runBlockingTest {
        MovieFactory.createDummyMovie().let {
            `when`(
                movieRepository
                    .getDetail(detailViewModel.contentId, BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            ).thenReturn(it)
            with(detailViewModel.movieDetail.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getMovieDetail(): $this")
            }
        }
    }

    @Test
    fun getTvDetail() = runBlockingTest {
        TvFactory.createDummyTv().let {
            `when`(
                tvRepository
                    .getDetail(detailViewModel.contentId, BuildConfig.TMDB_API_KEY, AppConfig.TMDB_API_DEFAULT_LANGUAGE)
            ).thenReturn(it)
            with(detailViewModel.tvDetail.getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                print("getTvDetail(): $this")
            }
        }
    }

    @Test
    fun getContentId() = runBlockingTest {
        assertEquals(0, detailViewModel.contentId)
        print("getLoaded(): ${detailViewModel.contentId}")
    }

    @Test
    fun setContentId() = runBlockingTest {
        (0..100).random().run {
            detailViewModel.contentId = this
            assertEquals(this, detailViewModel.contentId)
            print("setContentId(): $this")
        }
    }

    @Test
    fun checkIsFavouriteMovie() = runBlockingTest {
        MovieFactory.createDummyMovieEntity().let {
            `when`(movieRepository.checkIsFavourite(detailViewModel.contentId)).thenReturn(it)
            with(detailViewModel.checkIsFavouriteMovie(detailViewModel.contentId).getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                print("checkIsFavouriteMovie(): $this")
            }
        }
    }

    @Test
    fun checkIsFavouriteTv() = runBlockingTest {
        TvFactory.createDummyTvEntity().let {
            `when`(tvRepository.checkIsFavourite(detailViewModel.contentId)).thenReturn(it)
            with(detailViewModel.checkIsFavouriteTv(detailViewModel.contentId).getOrAwaitValue()) {
                assertNotNull(this)
                assertEquals(it, this)
                print("checkIsFavouriteTv(): $this")
            }
        }
    }

    @Test
    fun addMovieToFavourite() = runBlockingTest {
        MovieFactory.createDummyMovieEntity().let {
            (0..100).random().toLong().let { result ->
                `when`(movieRepository.addToFavourite(it)).thenReturn(result)
                with(detailViewModel.addMovieToFavourite(it).getOrAwaitValue()) {
                    assertEquals(result, this)
                    print("addMovieToFavourite(): $this")
                }
            }
        }
    }

    @Test
    fun addTvToFavourite() = runBlockingTest {
        TvFactory.createDummyTvEntity().let {
            (0..100).random().toLong().let { result ->
                `when`(tvRepository.addToFavourite(it)).thenReturn(result)
                with(detailViewModel.addTvToFavourite(it).getOrAwaitValue()) {
                    assertEquals(result, this)
                    print("addTvToFavourite(): $this")
                }
            }
        }
    }

    @Test
    fun removeMovieFromFavourite() = runBlockingTest {
        MovieFactory.createDummyMovieEntity().let {
            (0..1).random().let { result ->
                `when`(movieRepository.removeFromFavorite(it)).thenReturn(result)
                with(detailViewModel.removeMovieFromFavourite(it).getOrAwaitValue()) {
                    assertEquals(result, this)
                    print("removeMovieFromFavourite(): $this")
                }
            }
        }
    }

    @Test
    fun removeTvFromFavourite() = runBlockingTest {
        TvFactory.createDummyTvEntity().let {
            (0..1).random().let { result ->
                `when`(tvRepository.removeFromFavorite(it)).thenReturn(result)
                with(detailViewModel.removeTvFromFavourite(it).getOrAwaitValue()) {
                    assertEquals(result, this)
                    print("removeTvFromFavourite(): $this")
                }
            }
        }
    }

    @Test
    fun getFormattedDate() = runBlockingTest {
        TestUtils.getDummyDate().let {
            UUID.randomUUID().toString().let { dummyString ->
                `when`(detailViewModel.getFormattedDate(dummyString)).thenReturn(it)
                with(detailViewModel.getFormattedDate(dummyString)) {
                    assertEquals(it, this)
                    print("getFormattedDate(): $this")
                }
            }
        }
    }
}