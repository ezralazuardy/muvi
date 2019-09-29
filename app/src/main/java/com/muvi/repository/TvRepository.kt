package com.muvi.repository

import com.muvi.dao.local.LocalTvDao
import com.muvi.dao.remote.RemoteTvDao
import com.muvi.database.local.entity.TvEntity
import com.muvi.repository.base.BaseRepository
import org.jetbrains.anko.AnkoLogger

class TvRepository(
    private val localTvDao: LocalTvDao,
    private val remoteTvDao: RemoteTvDao
) : BaseRepository<TvEntity>, AnkoLogger {

    companion object {

        var tvRepository: TvRepository? = null

        fun getInstance(
            localMovieDao: LocalTvDao,
            remoteTvDao: RemoteTvDao
        ): TvRepository {
            val tempInstance = tvRepository
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = TvRepository(localMovieDao, remoteTvDao)
                tvRepository = instance
                return instance
            }
        }
    }

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
}