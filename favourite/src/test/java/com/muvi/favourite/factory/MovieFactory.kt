package com.muvi.favourite.factory

import com.muvi.favourite.model.entity.MovieEntity

object MovieFactory {

    fun createDummyMovieEntities(): List<MovieEntity> =
        with(mutableListOf<MovieEntity>()) {
            repeat(3) {
                add(
                    MovieEntity(
                        0, "dummy", "dummy", 1.0, "dummy",
                        "dummy"
                    )
                )
            }
            return this
        }
}