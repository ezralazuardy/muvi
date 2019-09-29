package com.muvi.dao.remote

import com.muvi.model.detail.Tv
import com.muvi.model.discover.DiscoverTvList
import com.muvi.model.genre.GenreList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteTvDao {

    @GET("discover/tv")
    suspend fun getDiscoverList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): DiscoverTvList

    @GET("genre/tv/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): GenreList

    @GET("tv/{id}")
    suspend fun getDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Tv
}