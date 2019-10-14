/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:53 AM
 */

package com.muvi.view.favourite.adapter.recyclerview.movie

import com.muvi.database.local.entity.MovieEntity

interface OnFavouriteMovieClickListener {

    fun onMovieItemClick(movieEntity: MovieEntity)
}