/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.dao.remote

import com.muvi.model.detail.Movie
import com.muvi.model.discover.DiscoverMovieList
import com.muvi.model.genre.GenreList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteMovieDao {

    @GET("search/movie")
    suspend fun search(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") title: String
    ): DiscoverMovieList

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

    @GET("discover/movie")
    suspend fun getRelease(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("primary_release_date.gte") gte: String,
        @Query("primary_release_date.lte") lte: String
    ): DiscoverMovieList
}