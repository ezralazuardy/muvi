package com.muvi.dao.local

import androidx.room.*
import com.muvi.database.local.entity.MovieEntity

@Dao
interface LocalMovieDao {

    @Query("SELECT * FROM movie_table ORDER BY id ASC")
    suspend fun getFavoriteList(): List<MovieEntity>

    @Query("SELECT * FROM movie_table WHERE id = :id LIMIT 1")
    suspend fun checkIsFavorite(id: Int): MovieEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addToFavorite(movieEntity: MovieEntity): Long

    @Delete
    suspend fun removeFromFavorite(movieEntity: MovieEntity): Int
}