package com.muvi.dao.local

import androidx.room.*
import com.muvi.database.local.entity.TvEntity

@Dao
interface LocalTvDao {

    @Query("SELECT * FROM tv_table ORDER BY id ASC")
    suspend fun getFavoriteList(): List<TvEntity>

    @Query("SELECT * FROM tv_table WHERE id = :id LIMIT 1")
    suspend fun checkIsFavorite(id: Int): TvEntity

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addToFavorite(tvEntity: TvEntity): Long

    @Delete
    suspend fun removeFromFavorite(tvEntity: TvEntity): Int
}