package com.muvi.view.favourite.adapter.tv

import com.muvi.database.local.entity.TvEntity

interface OnFavouriteTvClickListener {

    fun onTvItemClick(tvEntity: TvEntity)
}