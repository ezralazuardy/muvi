/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.provider

import android.net.Uri
import com.muvi.favourite.config.AppConfig

object DatabaseContract {

    private const val AUTHORITY  = "com.muvi.favourite.provider"
    private const val DATABASE = AppConfig.ROOM_DEFAULT_DATABASE_NAME
    val MOVIE_PROJECTION_MAP = arrayOf("id", "poster_path", "title", "vote_average", "original_language", "genres")
    val TV_PROJECTION_MAP = arrayOf("id", "poster_path", "title", "vote_average", "original_language", "genres")
    val MOVIE_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$DATABASE/${AppConfig.ROOM_DEFAULT_MOVIE_TABLE_NAME}")
    val TV_CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/$DATABASE/${AppConfig.ROOM_DEFAULT_TV_TABLE_NAME}")
}