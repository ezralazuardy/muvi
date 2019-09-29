package com.muvi.database.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "tv_table")
data class TvEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "poster_path") val poster_path: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "vote_average") val vote_average: Double,
    @ColumnInfo(name = "original_language") val original_language: String,
    @ColumnInfo(name = "genres") val genres: String
) : Parcelable