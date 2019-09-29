package com.muvi.view.list.adapter.movie

import com.muvi.model.discover.DiscoverMovieListResult

interface OnListMovieClickListener {

    fun onMovieItemClick(movie: DiscoverMovieListResult)
}