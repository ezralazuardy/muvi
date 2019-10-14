/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.repository

import com.muvi.favourite.dao.LocalMovieDao

class MovieRepository(private val localMovieDao: LocalMovieDao) {

    fun getFavouriteList() = localMovieDao.getFavoriteList()
}