/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.list.adapter.recyclerview.movie

import com.muvi.model.discover.DiscoverMovieListResult

interface OnListMovieClickListener {

    fun onMovieItemClick(movie: DiscoverMovieListResult)
}