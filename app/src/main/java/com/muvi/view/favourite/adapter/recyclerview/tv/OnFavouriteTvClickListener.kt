/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.favourite.adapter.recyclerview.tv

import com.muvi.database.local.entity.TvEntity

interface OnFavouriteTvClickListener {

    fun onTvItemClick(tvEntity: TvEntity)
}