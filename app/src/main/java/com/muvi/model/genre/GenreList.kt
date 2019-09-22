package com.muvi.model.genre

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GenreList(
    val genres: List<Genre>
) : Parcelable