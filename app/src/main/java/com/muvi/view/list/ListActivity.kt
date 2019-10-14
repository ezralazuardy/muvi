/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.list

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.config.ContentType
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.view.detail.DetailActivity
import com.muvi.view.favourite.FavouriteActivity
import com.muvi.view.list.adapter.recyclerview.movie.OnListMovieClickListener
import com.muvi.view.list.adapter.recyclerview.tv.OnListTvClickListener
import com.muvi.view.list.adapter.viewpager.ListActivityViewPagerAdapter
import com.muvi.view.search.SearchResultsActivity
import com.muvi.view.settings.SettingsActivity
import kotlinx.android.synthetic.main.activity_list.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton

class ListActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    OnListMovieClickListener,
    OnListTvClickListener,
    OnListFragmentChangeListener {

    private var currentFragmentType = ContentType.MOVIE
    private var searchView: SearchView? = null
    private val listActivityViewPagerAdapter by lazy {
        ListActivityViewPagerAdapter(supportFragmentManager, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setComponent()
    }

    private fun setComponent() {
        setSupportActionBar(toolBar)
        supportActionBar?.title = null
        viewPager.adapter = listActivityViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val drawable: Drawable? = ContextCompat.getDrawable(
                this,
                ListActivityViewPagerAdapter.tabIcons[i]
            )
            drawable?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            tabLayout.getTabAt(i)?.icon = drawable
        }
    }

    private fun setSearchMovie() {
        currentFragmentType = ContentType.MOVIE
        searchView?.queryHint = getString(R.string.search_hint_movie)
    }

    private fun setSearchTv() {
        currentFragmentType = ContentType.TV
        searchView?.queryHint = getString(R.string.search_hint_tv)
    }

    override fun onMovieItemClick(movie: DiscoverMovieListResult) {
        startActivity<DetailActivity>(
            AppConfig.INTENT_EXTRA_CONTENT_TYPE to ContentType.MOVIE,
            AppConfig.INTENT_EXTRA_DATA_MOVIE to movie,
            AppConfig.INTENT_EXTRA_OPEN_FROM to this.javaClass.simpleName
        )
    }

    override fun onTvItemClick(tv: DiscoverTvListResult) {
        startActivity<DetailActivity>(
            AppConfig.INTENT_EXTRA_CONTENT_TYPE to ContentType.TV,
            AppConfig.INTENT_EXTRA_DATA_TV to tv,
            AppConfig.INTENT_EXTRA_OPEN_FROM to this.javaClass.simpleName
        )
    }

    override fun onBackPressed() {
        alert(resources.getString(R.string.dialog_confirm_exit_app)) {
            yesButton { super.onBackPressed() }
            noButton { }
        }.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_list, menu)
        menu?.let {
            val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
            searchView = (menu.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo(searchManager.getSearchableInfo(componentName))
                setOnQueryTextListener(this@ListActivity)
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favourite -> {
                startActivity<FavouriteActivity>()
                true
            }
            R.id.settings -> {
                startActivity<SettingsActivity>()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onFragmentChange(contentType: ContentType) {
        when(contentType) {
            ContentType.MOVIE -> setSearchMovie()
            ContentType.TV -> setSearchTv()
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        when(currentFragmentType) {
            ContentType.MOVIE -> startActivity<SearchResultsActivity>(
                AppConfig.INTENT_EXTRA_CONTENT_TYPE to ContentType.MOVIE,
                AppConfig.INTENT_EXTRA_SEARCH_QUERY to query,
                AppConfig.INTENT_EXTRA_OPEN_FROM to this::class.java.simpleName
            )
            ContentType.TV -> startActivity<SearchResultsActivity>(
                AppConfig.INTENT_EXTRA_CONTENT_TYPE to ContentType.TV,
                AppConfig.INTENT_EXTRA_SEARCH_QUERY to query,
                AppConfig.INTENT_EXTRA_OPEN_FROM to this::class.java.simpleName
            )
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true
}