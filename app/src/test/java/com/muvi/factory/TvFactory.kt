package com.muvi.factory

import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre

object TvFactory {

    fun createDummyTvs(): List<DiscoverTvListResult> =
        with(mutableListOf<DiscoverTvListResult>()) {
            repeat(3) {
                add(
                    DiscoverTvListResult(
                        "dummy", "dummy", listOf(0), 0, "dummy", listOf("dummy"),
                        "dummy", "dummy", "dummy", 1.0,
                        "dummy", 1.0, 0
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