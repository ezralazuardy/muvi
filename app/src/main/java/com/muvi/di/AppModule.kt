/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.di

import com.muvi.database.local.LocalDatabase
import com.muvi.database.remote.RemoteDatabase
import com.muvi.repository.MovieRepository
import com.muvi.repository.SettingsRepository
import com.muvi.repository.TvRepository
import com.muvi.repository.utils.UtilsRepository
import com.muvi.viewmodel.detail.DetailViewModel
import com.muvi.viewmodel.favourite.FavouriteViewModel
import com.muvi.viewmodel.list.ListViewModel
import com.muvi.viewmodel.search.SearchViewModel
import com.muvi.viewmodel.settings.SettingsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single { MovieRepository(LocalDatabase.getDatabase(get()).movieDao(), RemoteDatabase.movieDao()) }

    single { TvRepository(LocalDatabase.getDatabase(get()).tvDao(), RemoteDatabase.tvDao()) }

    single { SettingsRepository(get()) }

    single { UtilsRepository(get()) }

    viewModel { ListViewModel(get(), get(), get()) }

    viewModel { DetailViewModel(get(), get(), get(), get()) }

    viewModel { FavouriteViewModel(get(), get(), get()) }

    viewModel { SettingsViewModel(get(), get(), get()) }

    viewModel { SearchViewModel(get(), get(), get()) }
}