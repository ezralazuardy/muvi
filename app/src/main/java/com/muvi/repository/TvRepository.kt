/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.repository

import android.database.Cursor
import com.muvi.dao.local.LocalTvDao
import com.muvi.dao.remote.RemoteTvDao
import com.muvi.database.local.entity.TvEntity
import com.muvi.repository.base.TMDBRepository
import org.jetbrains.anko.AnkoLogger

class TvRepository(
    private val localTvDao: LocalTvDao,
    private val remoteTvDao: RemoteTvDao
) : TMDBRepository<TvEntity>, AnkoLogger {

    override suspend fun search(apiKey: String, language: String, title: String) =
        remoteTvDao.search(
            apiKey,
            language,
            title
        ).results.filter { it.original_name.isNotEmpty() }

    override suspend fun getDiscoverList(apiKey: String, language: String, page: Int) =
        remoteTvDao.getDiscoverList(
            apiKey,
            language,
            page
        ).results.filter { !it.poster_path.isNullOrEmpty() }

    override suspend fun getGenres(apiKey: String, language: String) =
        remoteTvDao.getGenres(apiKey, language).genres.filter { !it.name.isNullOrEmpty() }

    override suspend fun getDetail(id: Int, apiKey: String, language: String) =
        remoteTvDao.getDetail(id, apiKey, language)

    override suspend fun getFavouriteList() =
        localTvDao.getFavoriteList()

    override suspend fun checkIsFavourite(id: Int) =
        localTvDao.checkIsFavorite(id)

    override suspend fun addToFavourite(data: TvEntity) =
        localTvDao.addToFavorite(data)

    override suspend fun removeFromFavorite(data: TvEntity) =
        localTvDao.removeFromFavorite(data)

    override fun getFavouriteListCursorSynchronous(): Cursor =
        localTvDao.getFavouriteListCursorSynchronous()
}