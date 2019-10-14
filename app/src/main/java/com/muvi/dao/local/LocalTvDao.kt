/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.dao.local

import android.database.Cursor
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

    @Query("SELECT * FROM tv_table ORDER BY id ASC")
    fun getFavouriteListCursorSynchronous(): Cursor
}