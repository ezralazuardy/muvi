package com.muvi.repository

import com.muvi.dao.local.LocalMovieDao
import com.muvi.dao.remote.RemoteMovieDao
import com.muvi.database.local.entity.MovieEntity
import com.muvi.repository.base.BaseRepository
import org.jetbrains.anko.AnkoLogger

class MovieRepository(
    private val localMovieDao: LocalMovieDao,
    private val remoteMovieDao: RemoteMovieDao
) : BaseRepository<MovieEntity>, AnkoLogger {

    companion object {

        var movieRepository: MovieRepository? = null

        fun getInstance(
            localMovieDao: LocalMovieDao,
            remoteMovieDao: RemoteMovieDao
        ): MovieRepository {
            val tempInstance = movieRepository
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = MovieRepository(localMovieDao, remoteMovieDao)
                movieRepository = instance
                return instance
            }
        }
    }

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
}