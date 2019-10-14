/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.widget

import android.content.Context
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.database.local.entity.MovieEntity
import com.muvi.repository.MovieRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory, KoinComponent {

    private var movies = arrayListOf<MovieEntity>()
    private val movieRepository: MovieRepository by inject()

    override fun onCreate() { }

    override fun onDataSetChanged() {
        movies.clear()
        movies.addAll(movieRepository.getFavouriteListSynchronous())
        Binder.restoreCallingIdentity(Binder.clearCallingIdentity())
    }

    override fun onDestroy() { }

    override fun getCount() = movies.size

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.item_list_stack_view_widget).apply {
            setImageViewBitmap(
                R.id.imageView,
                Glide.with(context)
                    .asBitmap()
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + movies[position].poster_path)
                    .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(10)))
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .submit(220, 260)
                    .get()
            )
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount() = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds() = false
}
