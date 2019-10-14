/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.config

object AppConfig {

    const val TMDB_API_BASE_URL: String = "https://api.themoviedb.org/3/"
    const val TMDB_API_IMAGE_BASE_URL: String = "https://image.tmdb.org/t/p/w342/"
    const val TMDB_API_DEFAULT_LANGUAGE = "en-US"
    const val TMDB_DATE_FORMAT = "yyyy-MM-dd"

    const val ROOM_DEFAULT_DATABASE_NAME = "local_database"
    const val ROOM_DEFAULT_MOVIE_TABLE_NAME = "movie_table"
    const val ROOM_DEFAULT_TV_TABLE_NAME = "tv_table"

    const val DEFAULT_DATE_FORMAT = "dd MMMM yyyy"
    const val DEFAULT_TIME_FORMAT = "HH:mm"
    const val DEFAULT_TIME_FORMAT_12 = "hh:mm a"

    const val DEFAULT_REMINDER_TIME = "07:00"
    const val DEFAULT_NEW_RELEASE_TIME = "08:00"

    const val SHARED_PREFERENCE_APP_SETTINGS_REMAINDER = "app_settings_remainder"
    const val SHARED_PREFERENCE_APP_SETTINGS_REMAINDER_TIME = "app_settings_remainder_time"
    const val SHARED_PREFERENCE_APP_SETTINGS_NEW_RELEASE = "app_settings_new_release"

    const val WORKER_REMINDER_NAME = "worker_reminder"

    const val INTENT_EXTRA_CONTENT_TYPE = "type"
    const val INTENT_EXTRA_DATA_MOVIE = "movie"
    const val INTENT_EXTRA_DATA_TV = "tv"
    const val INTENT_EXTRA_OPEN_FROM = "openFrom"
    const val INTENT_EXTRA_SEARCH_QUERY = "query"
}