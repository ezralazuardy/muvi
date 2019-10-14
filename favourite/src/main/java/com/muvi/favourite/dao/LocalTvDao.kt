/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.dao

import android.content.Context
import android.net.Uri
import com.muvi.favourite.model.entity.TvEntity
import com.muvi.favourite.provider.DatabaseContract

class LocalTvDao(private val context: Context) {

    fun getFavoriteList(): List<TvEntity>? {
        val cursor = context.contentResolver.query(
            Uri.parse(DatabaseContract.TV_CONTENT_URI.toString()),
            DatabaseContract.TV_PROJECTION_MAP,
            null,
            null,
            null
        )
        cursor?.let {
            if(cursor.count > 0) {
                cursor.moveToFirst()
                val tvs = mutableListOf<TvEntity>()
                do {
                    tvs.add(
                        TvEntity(
                            cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[0])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[1])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[2])),
                            cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[3])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[4])),
                            cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.TV_PROJECTION_MAP[5]))
                        )
                    )
                } while (cursor.moveToNext())
                return tvs
            }
            cursor.close()
        }
        return null
    }
}