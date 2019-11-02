package com.muvi.factory

import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.genre.Genre

object MovieFactory {

    fun createDummyMovies(): List<DiscoverMovieListResult> =
        with(mutableListOf<DiscoverMovieListResult>()) {
            repeat(3) {
                add(
                    DiscoverMovieListResult(
                        false, "dummy", listOf(0), 0, "dummy", "dummy",
                        "dummy", 1.0, "dummy", "dummy", "dummy",
                        false, 1.0, 0
                    )
                )
            }
            return this
        }

    fun createDummyGenres(): List<Genre> =
        with(mutableListOf<Genre>()) {
            repeat(5) { add(Genre(0, "dummy")) }
            return this
        }
}