/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.repository

import android.database.Cursor
import com.muvi.dao.local.LocalMovieDao
import com.muvi.dao.remote.RemoteMovieDao
import com.muvi.database.local.entity.MovieEntity
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.genre.Genre
import com.muvi.repository.base.TMDBRepository
import org.jetbrains.anko.AnkoLogger

class MovieRepository(
    private val localMovieDao: LocalMovieDao,
    private val remoteMovieDao: RemoteMovieDao
) : TMDBRepository<MovieEntity>, AnkoLogger {

    override suspend fun search(apiKey: String, language: String, title: String) =
        remoteMovieDao.search(
            apiKey,
            language,
            title
        ).results.filter { it.original_title.isNotEmpty() }

    override suspend fun getDiscoverList(apiKey: String, language: String, page: Int) =
        remoteMovieDao.getDiscoverList(
            apiKey,
            language,
            page
        ).results.filter { !it.poster_path.isNullOrEmpty() }

    override suspend fun getGenres(apiKey: String, language: String) =
        remoteMovieDao.getGenres(apiKey, language).genres.filter { !it.name.isNullOrEmpty() }

    override suspend fun getDetail(id: Int, apiKey: String, language: String) =
        remoteMovieDao.getDetail(id, apiKey, language)

    override suspend fun getFavouriteList() =
        localMovieDao.getFavoriteList()

    override suspend fun checkIsFavourite(id: Int) =
        localMovieDao.checkIsFavorite(id)

    override suspend fun addToFavourite(data: MovieEntity) =
        localMovieDao.addToFavorite(data)

    override suspend fun removeFromFavorite(data: MovieEntity) =
        localMovieDao.removeFromFavorite(data)

    override fun getFavouriteListCursorSynchronous(): Cursor =
        localMovieDao.getFavouriteListCursorSynchronous()

    suspend fun getRelease(
        apiKey: String,
        language: String,
        gte: String,
        lte: String
    ) = remoteMovieDao.getRelease(apiKey, language, gte, lte)

    fun getFavouriteListSynchronous() =
        localMovieDao.getFavouriteListSynchronous()

    fun getDummyDiscoverList(): List<DiscoverMovieListResult> = listOf()

    fun getDummyGenres(): List<Genre> = listOf()
}