/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.viewmodel.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers

class ListViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : AndroidViewModel(application), OnLoadViewModel {

    override var loaded = false
    val discoverMovies: LiveData<List<DiscoverMovieListResult>> by lazy { discoverMovies() }
    val discoverTvs: LiveData<List<DiscoverTvListResult>> by lazy { discoverTvs() }
    val movieGenres: LiveData<List<Genre>> by lazy { movieGenres() }
    val tvGenres: LiveData<List<Genre>> by lazy { tvGenres() }

    private fun discoverMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE,
        page: Int = 1
    ) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.getDiscoverList(apiKey, language, page))
        }

    private fun discoverTvs(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE,
        page: Int = 1
    ) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.getDiscoverList(apiKey, language, page))
        }

    private fun movieGenres(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE
    ) =
        liveData(Dispatchers.IO) {
            emit(movieRepository.getGenres(apiKey, language))
        }

    private fun tvGenres(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE
    ) =
        liveData(Dispatchers.IO) {
            emit(tvRepository.getGenres(apiKey, language))
        }
}