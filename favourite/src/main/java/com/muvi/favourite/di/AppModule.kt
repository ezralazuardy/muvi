/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.di

import com.muvi.favourite.dao.LocalMovieDao
import com.muvi.favourite.dao.LocalTvDao
import com.muvi.favourite.repository.MovieRepository
import com.muvi.favourite.repository.TvRepository
import com.muvi.favourite.viewmodel.FavouriteListViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { LocalMovieDao(get()) }

    single { LocalTvDao(get()) }

    single { MovieRepository(get()) }

    single { TvRepository(get()) }

    viewModel { FavouriteListViewModel(get(), get(), get()) }
}