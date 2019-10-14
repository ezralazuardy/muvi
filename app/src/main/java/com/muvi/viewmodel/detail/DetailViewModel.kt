/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.viewmodel.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.database.local.entity.MovieEntity
import com.muvi.database.local.entity.TvEntity
import com.muvi.model.detail.Movie
import com.muvi.model.detail.Tv
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.repository.utils.UtilsRepository
import com.muvi.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers
import org.jetbrains.anko.AnkoLogger
import org.koin.core.KoinComponent

class DetailViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository,
    private val utilsRepository: UtilsRepository
) : AndroidViewModel(application), KoinComponent, AnkoLogger, OnLoadViewModel {

    override var loaded = false
    val movieDetail: LiveData<Movie> by lazy { getMovieDetail(contentId) }
    val tvDetail: LiveData<Tv> by lazy { getTvDetail(contentId) }
    var contentId: Int = 0

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
            utilsRepository.refreshFavouriteWidget()
        }

    fun addTvToFavourite(tv: TvEntity) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.addToFavourite(tv))
            utilsRepository.refreshFavouriteWidget()
        }

    fun removeMovieFromFavourite(movieEntity: MovieEntity) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.removeFromFavorite(movieEntity))
            utilsRepository.refreshFavouriteWidget()
        }

    fun removeTvFromFavourite(tvEntity: TvEntity) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.removeFromFavorite(tvEntity))
            utilsRepository.refreshFavouriteWidget()
        }

    fun getFormattedDate(date: String) =
        utilsRepository.formatDate(AppConfig.DEFAULT_DATE_FORMAT, date)
}