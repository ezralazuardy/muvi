/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.repository.base

interface TMDBRepository<T> {

    suspend fun search(
        apiKey: String,
        language: String,
        title: String
    ) : Any

    suspend fun getDiscoverList(
        apiKey: String,
        language: String,
        page: Int = 1
    ): Any

    suspend fun getGenres(
        apiKey: String,
        language: String
    ): Any

    suspend fun getDetail(
        id: Int,
        apiKey: String,
        language: String
    ): Any

    suspend fun getFavouriteList(): Any

    suspend fun addToFavourite(data: T): Any

    suspend fun checkIsFavourite(
        id: Int
    ): Any

    suspend fun removeFromFavorite(
        data: T
    ): Int

    fun getFavouriteListCursorSynchronous(): Any
}