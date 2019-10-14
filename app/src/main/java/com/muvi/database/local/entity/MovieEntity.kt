/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.database.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.muvi.config.AppConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = AppConfig.ROOM_DEFAULT_MOVIE_TABLE_NAME)
data class MovieEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vote_average") val vote_average: Double,
    @ColumnInfo(name = "original_language") val original_language: String,
    @ColumnInfo(name = "genres") val genres: String
) : Parcelable