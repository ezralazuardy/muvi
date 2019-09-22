package com.muvi.view.adapter.movie

import com.muvi.model.discover.DiscoverMovieListResult

interface OnMovieClickListener {
    fun onMovieItemClick(movie: DiscoverMovieListResult)
}