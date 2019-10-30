/*
 * Created by Ezra Lazuardy on 10/31/19, 12:23 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/30/19, 10:27 PM
 */

package com.muvi.viewmodel.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muvi.database.local.entity.MovieEntity
import com.muvi.database.local.entity.TvEntity
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import com.muvi.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(
    application: Application,
    private val movieRepository: MovieRepository,
    private val tvRepository: TvRepository
) : AndroidViewModel(application), OnLoadViewModel {

    override var loaded = false
    private val _favouriteMovies = MutableLiveData<List<MovieEntity>>()
    private val _favouriteTvs = MutableLiveData<List<TvEntity>>()
    val favouriteMovies: LiveData<List<MovieEntity>> = _favouriteMovies
    val favouriteTvs: LiveData<List<TvEntity>> = _favouriteTvs

    fun getFavouriteMovies() =
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteMovies.postValue(movieRepository.getFavouriteList())
        }

    fun getFavouriteTvs() =
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteTvs.postValue(tvRepository.getFavouriteList())
        }
}