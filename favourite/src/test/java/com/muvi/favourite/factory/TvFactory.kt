package com.muvi.favourite.factory

import com.muvi.favourite.model.entity.TvEntity

object TvFactory {

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