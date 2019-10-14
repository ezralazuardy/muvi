/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.dao

import android.content.Context
import android.net.Uri
import com.muvi.favourite.model.entity.MovieEntity
import com.muvi.favourite.provider.DatabaseContract

class LocalMovieDao(private val context: Context) {

    fun getFavoriteList(): List<MovieEntity>? {
        val cursor = context.contentResolver.query(
            Uri.parse(DatabaseContract.MOVIE_CONTENT_URI.toString()),
            DatabaseContract.MOVIE_PROJECTION_MAP,
            null,
            null,
            null
        )
        cursor?.let {
            if(cursor.count > 0) {
                cursor.moveToFirst()
                val movies = mutableListOf<MovieEntity>()
                do {
                    movies.add(
                        MovieEntity(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[0])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[1])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[2])),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[3])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[4])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.MOVIE_PROJECTION_MAP[5]))
                        )
                    )
                } while (cursor.moveToNext())
                return movies
            }
            cursor.close()
        }
        return null
    }
}