package com.muvi.factory

import com.muvi.database.local.entity.TvEntity
import com.muvi.model.detail.LastEpisodeToAir
import com.muvi.model.detail.NextEpisodeToAir
import com.muvi.model.detail.Tv
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre

internal object TvFactory {

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

    fun createDummyTv(): Tv =
        Tv(
            "dummy", listOf(), listOf(), "dummy", listOf(), "dummy", 0,
            false, listOf(), "dummy", LastEpisodeToAir(
                "dummy", 0,
                0, "dummy", "dummy", "dummy", 0, 0,
                "dummy", 1.0, 0
            ), "dummy", listOf(),
            NextEpisodeToAir(
                "dummy", 0, 0, "dummy", "dummy",
                "dummy", 0, 0, "dummy", 1.0, 0
            ),
            0, 0, listOf(), "dummy", "dummy",
            "dummy", 1.0, "dummy", listOf(), listOf(), "dummy", "dummy",
            1.0, 0
        )

    fun createDummyTvEntity(): TvEntity =
        TvEntity(0, "dummy", "dummy", 1.0, "dummy", "dummy")

    fun createDummyTvEntities(): List<TvEntity> =
        with(mutableListOf<TvEntity>()) {
            repeat(3) {
                add(
                    TvEntity(
                        0, "dummy", "dummy", 1.0, "dummy",
                        "dummy"
                    )
                )
            }
            return this
        }
}