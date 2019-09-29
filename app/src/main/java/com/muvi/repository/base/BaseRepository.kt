package com.muvi.repository.base

interface BaseRepository<T> {

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
}