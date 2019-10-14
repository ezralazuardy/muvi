/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.muvi.config.AppConfig
import com.muvi.repository.MovieRepository
import com.muvi.repository.TvRepository
import org.koin.android.ext.android.inject

class FavouriteProvider : ContentProvider() {

    private val movieRepository: MovieRepository by inject()
    private val tvRepository: TvRepository by inject()

    companion object {
        private const val AUTHORITY = "com.muvi.favourite.provider"
        private const val DATABASE = AppConfig.ROOM_DEFAULT_DATABASE_NAME
        private const val CONTENT_TYPE_MOVIE = 1
        private const val CONTENT_TYPE_TV = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            uriMatcher.addURI(AUTHORITY, "$DATABASE/${AppConfig.ROOM_DEFAULT_MOVIE_TABLE_NAME}", CONTENT_TYPE_MOVIE)
            uriMatcher.addURI(AUTHORITY, "$DATABASE/${AppConfig.ROOM_DEFAULT_TV_TABLE_NAME}", CONTENT_TYPE_TV)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? = when (uriMatcher.match(uri)) {
        CONTENT_TYPE_MOVIE -> movieRepository.getFavouriteListCursorSynchronous()
        CONTENT_TYPE_TV -> tvRepository.getFavouriteListCursorSynchronous()
        else -> null
    }

    override fun onCreate(): Boolean = true

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}