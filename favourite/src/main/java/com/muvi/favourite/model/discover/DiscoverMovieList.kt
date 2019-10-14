/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.model.discover

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiscoverMovieList(
    val page: Int,
    val results: List<DiscoverMovieListResult>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable