package com.muvi.factory

import com.muvi.database.local.entity.MovieEntity
import com.muvi.model.detail.BelongsToCollection
import com.muvi.model.detail.Movie
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.genre.Genre

object MovieFactory {

    fun createDummyMovies(): List<DiscoverMovieListResult> =
        with(mutableListOf<DiscoverMovieListResult>()) {
            repeat(3) {
                add(
                    DiscoverMovieListResult(
                        false, "dummy", listOf(0), 0, "dummy",
                        "dummy", "dummy", 1.0, "dummy", "dummy",
                        "dummy", false, 1.0, 0
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

    fun createDummyMovie(): Movie =
        Movie(
            false, "dummy",
            BelongsToCollection("dummy", 0, "dummy", "dummy"), 0, listOf(),
            "dummy", 0, "dummy", "dummy", "dummy", "dummy",
            1.0, "dummy", listOf(), listOf(), "dummy", "dummy", 0,
            listOf(), "dummy", "dummy", "dummy", false, 1.0, 0
        )

    fun createDummyMovieEntity(): MovieEntity =
        MovieEntity(0, "dummy", "dummy", 1.0, "dummy", "dummy")

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