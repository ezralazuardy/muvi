/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.viewmodel.search

import android.app.Application
import androidx.lifecycle.*
import com.muvi.BuildConfig
import com.muvi.config.AppConfig
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : AndroidViewModel(application), OnLoadViewModel {

    override var loaded = false
    private val _searchMovies = MutableLiveData<List<DiscoverMovieListResult>>()
    private val _searchTvs = MutableLiveData<List<DiscoverTvListResult>>()
    val searchMovies: LiveData<List<DiscoverMovieListResult>> = _searchMovies
    val searchTvs: LiveData<List<DiscoverTvListResult>> = _searchTvs
    val movieGenres: LiveData<List<Genre>> by lazy { movieGenres() }
    val tvGenres: LiveData<List<Genre>> by lazy { tvGenres() }

    fun searchMovies(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE,
        query: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            _searchMovies.postValue(movieRepository.search(apiKey, language, query))
        }

    fun searchTvs(
        apiKey: String = BuildConfig.TMDB_API_KEY,
        language: String = AppConfig.TMDB_API_DEFAULT_LANGUAGE,
        query: String
    ) =
        viewModelScope.launch(Dispatchers.IO) {
            _searchTvs.postValue(tvRepository.search(apiKey, language, query))
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