package com.muvi.model.discover

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DiscoverMovieList(
    val page: Int,
    val results: List<DiscoverMovieListResult>,
    val total_pages: Int,
    val total_results: Int
) : Parcelable