package com.muvi.viewmodel.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.database.local.LocalDatabase
import com.muvi.database.local.entity.MovieEntity
import com.muvi.database.local.entity.TvEntity
import com.muvi.database.remote.RemoteDatabase
import com.muvi.model.detail.Movie
import com.muvi.model.detail.Tv
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.anko.AnkoLogger

class DetailViewModel(
    application: Application,
    contentId: Int
) : AndroidViewModel(application), AnkoLogger, OnLoadViewModel {

    override var loaded = false
    private val movieRepository: MovieRepository = MovieRepository.getInstance(
        LocalDatabase.getDatabase(application).movieDao(),
        RemoteDatabase.movieDao()
    )
    private val tvRepository: TvRepository = TvRepository.getInstance(
        LocalDatabase.getDatabase(application).tvDao(),
        RemoteDatabase.tvDao()
    )
    val movieDetail: LiveData<Movie> by lazy { getMovieDetail(contentId) }
    val tvDetail: LiveData<Tv> by lazy { getTvDetail(contentId) }

    private fun getMovieDetail(
        id: Int,
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE
    ): LiveData<Movie> =
        liveData(Dispatchers.IO) {
            emit(movieRepository.getDetail(id, apiKey, language))
        }

    private fun getTvDetail(
        id: Int,
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE
    ) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.getDetail(id, apiKey, language))
        }

    fun checkIsFavouriteMovie(id: Int) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.checkIsFavourite(id))
        }

    fun checkIsFavouriteTv(id: Int) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.checkIsFavourite(id))
        }

    fun addMovieToFavourite(movie: MovieEntity) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.addToFavourite(movie))
        }

    fun addTvToFavourite(tv: TvEntity) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.addToFavourite(tv))
        }

    fun removeMovieFromFavourite(movieEntity: MovieEntity) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.removeFromFavorite(movieEntity))
        }

    fun removeTvFromFavourite(tvEntity: TvEntity) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.removeFromFavorite(tvEntity))
        }
}