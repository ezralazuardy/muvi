package com.muvi.view.favourite.adapter.movie

import com.muvi.database.local.entity.MovieEntity

interface OnFavouriteMovieClickListener {

    fun onMovieItemClick(movieEntity: MovieEntity)
}