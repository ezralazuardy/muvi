/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.repository

import com.muvi.favourite.dao.LocalTvDao

class TvRepository(private val localTvDao: LocalTvDao) {

    suspend fun getFavouriteList() = localTvDao.getFavoriteList()
}