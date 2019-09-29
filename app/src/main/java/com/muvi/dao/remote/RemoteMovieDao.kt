package com.muvi.dao.remote

import com.muvi.model.detail.Movie
import com.muvi.model.discover.DiscoverMovieList
import com.muvi.model.genre.GenreList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteMovieDao {

    @GET("discover/movie")
    suspend fun getDiscoverList(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): DiscoverMovieList

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): GenreList

    @GET("movie/{id}")
    suspend fun getDetail(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Movie
}