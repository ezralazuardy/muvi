/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.muvi.favourite.model.entity.MovieEntity
import com.muvi.favourite.model.entity.TvEntity
import com.muvi.favourite.repository.MovieRepository
import com.muvi.favourite.repository.TvRepository
import com.muvi.favourite.viewmodel.base.OnLoadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteListViewModel(
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