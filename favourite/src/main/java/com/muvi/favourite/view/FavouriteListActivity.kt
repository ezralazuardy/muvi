/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.favourite.view

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.muvi.favourite.R
import com.muvi.favourite.model.entity.MovieEntity
import com.muvi.favourite.model.entity.TvEntity
import com.muvi.favourite.view.adapter.recyclerview.movie.OnFavouriteMovieClickListener
import com.muvi.favourite.view.adapter.recyclerview.tv.OnFavouriteTvClickListener
import com.muvi.favourite.view.adapter.viewpager.FavouriteListActivityViewPagerAdapter
import kotlinx.android.synthetic.main.activity_favourite_list.*
import org.jetbrains.anko.design.snackbar

class FavouriteListActivity : AppCompatActivity(), OnFavouriteMovieClickListener,
    OnFavouriteTvClickListener {


    private val favouriteListActivityViewPagerAdapter by lazy {
        FavouriteListActivityViewPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_list)
        setComponent()
    }

    private fun setComponent() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = null
        viewPager.adapter = favouriteListActivityViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val drawable: Drawable? = ContextCompat.getDrawable(
                this,
                FavouriteListActivityViewPagerAdapter.tabIcons[i]
            )
            drawable?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            tabLayout.getTabAt(i)?.icon = drawable
        }
    }

    override fun onMovieItemClick(movieEntity: MovieEntity) {
        layoutFavouriteListActivity.snackbar(getString(R.string.snackbar_feature_not_ready))
    }

    override fun onTvItemClick(tvEntity: TvEntity) {
        layoutFavouriteListActivity.snackbar(getString(R.string.snackbar_feature_not_ready))
    }
}
