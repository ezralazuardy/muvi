/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.database.remote

import com.muvi.api.Retrofit
import com.muvi.config.AppConfig
import com.muvi.dao.remote.RemoteMovieDao
import com.muvi.dao.remote.RemoteTvDao

object RemoteDatabase {

    fun movieDao(): RemoteMovieDao =
        Retrofit.getClient(AppConfig.TMDB_API_BASE_URL).create(RemoteMovieDao::class.java)

    fun tvDao(): RemoteTvDao =
        Retrofit.getClient(AppConfig.TMDB_API_BASE_URL).create(RemoteTvDao::class.java)
}