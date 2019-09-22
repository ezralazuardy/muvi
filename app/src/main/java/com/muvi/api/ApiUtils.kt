package com.muvi.api

import com.muvi.api.service.TMDBApiService
import com.muvi.config.AppConfig

object ApiUtils {

    fun getTMDBApiService(): TMDBApiService {
        return Retrofit.getClient(AppConfig.TMDB_API_BASE_URL).create(TMDBApiService::class.java)
    }
}