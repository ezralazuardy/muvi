package com.muvi.api.service

import com.muvi.BuildConfig
import com.muvi.model.detail.Movie
import com.muvi.model.detail.Tv
import com.muvi.model.discover.DiscoverMovieList
import com.muvi.model.discover.DiscoverTvList
import com.muvi.model.genre.GenreList
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApiService {

    @GET("movie/{movie_id}")
    fun getMovieDetail(
        @Path("movie_id") movieId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US"
    ): Single<Movie>

    @GET("tv/{tv_id}")
    fun getTvDetail(
        @Path("tv_id") tvId: String,
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US"
    ): Single<Tv>

    @GET("genre/movie/list")
    fun getMovieGenreList(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US"
    ): Single<GenreList>

    @GET("genre/tv/list")
    fun getTvGenreList(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US"
    ): Single<GenreList>

    @GET("discover/movie")
    fun discoverMovies(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Single<DiscoverMovieList>

    @GET("discover/tv")
    fun discoverTvs(
        @Query("api_key") apiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: Int
    ): Single<DiscoverTvList>
}