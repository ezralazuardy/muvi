/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.search

import android.app.ListActivity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.config.ContentType
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.repository.utils.UtilsRepository
import com.muvi.view.detail.DetailActivity
import com.muvi.view.search.adapter.recyclerview.movie.OnSearchResultsMovieClickListener
import com.muvi.view.search.adapter.recyclerview.movie.SearchResultsMovieAdapter
import com.muvi.view.search.adapter.recyclerview.tv.OnSearchResultsTvClickListener
import com.muvi.view.search.adapter.recyclerview.tv.SearchResultsTvAdapter
import com.muvi.viewmodel.search.SearchViewModel
import kotlinx.android.synthetic.main.activity_search_results.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject

class SearchResultsActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    AnkoLogger,
    OnSearchResultsMovieClickListener,
    OnSearchResultsTvClickListener {

    private val searchResultsMovieAdapter by lazy { SearchResultsMovieAdapter() }
    private val searchResultsTvAdapter by lazy { SearchResultsTvAdapter() }
    private var contentType = ContentType.MOVIE
    private var searchView: SearchView? = null
    private var query: String? = null
    private val searchViewModel: SearchViewModel by inject()
    private val utilsRepository: UtilsRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        intent?.let { handleIntent(intent) }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { handleIntent(intent) }
    }

    private fun handleIntent(intent: Intent) {
        val type = intent.getSerializableExtra(AppConfig.INTENT_EXTRA_CONTENT_TYPE)
        val query = intent.getStringExtra(AppConfig.INTENT_EXTRA_SEARCH_QUERY)
        val openFrom = intent.getStringExtra(AppConfig.INTENT_EXTRA_OPEN_FROM)
        if(type != null && query != null && openFrom != null) {
            contentType = type as ContentType
            this.query = query
            when(openFrom) {
                ListActivity::class.java.simpleName -> setContent()
                else -> {
                    error { "SearchResultActivity is unsupported when opened from $openFrom" }
                    finish()
                }
            }
        } else {
            error { "SearchResultActivity is receiving null intent" }
            finish()
        }
    }

    private fun setContent() {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SearchResultsActivity)
            setHasFixedSize(true)
        }
        searchView?.setOnQueryTextListener(this)
        when(contentType) {
            ContentType.MOVIE -> {
                supportActionBar?.apply {
                    title = getString(R.string.search_hint_movie)
                    setIcon(R.drawable.ic_movies_white)
                    recyclerView.adapter = searchResultsMovieAdapter
                    searchResultsMovieAdapter.notifyDataSetChanged()
                    observeFavouriteMovies()
                }
            }
            ContentType.TV -> {
                supportActionBar?.apply {
                    title = getString(R.string.search_hint_tv)
                    setIcon(R.drawable.ic_movies_white)
                    recyclerView.adapter = searchResultsTvAdapter
                    searchResultsTvAdapter.notifyDataSetChanged()
                    observeFavouriteTvs()
                }
            }
        }
    }

    private fun observeFavouriteMovies() {
        searchViewModel.movieGenres.observe(this, Observer { genres ->
            searchViewModel.searchMovies.observe(this, Observer {
                if(!searchViewModel.loaded) searchViewModel.loaded = true
                if(!it.isNullOrEmpty()) searchResultsMovieAdapter.setData(it, genres, this)
                showTextResult()
                hideLoading()
            })
        })
    }

    private fun observeFavouriteTvs() {
        searchViewModel.tvGenres.observe(this, Observer { genres ->
            searchViewModel.searchTvs.observe(this, Observer {
                if(!searchViewModel.loaded) searchViewModel.loaded = true
                if(!it.isNullOrEmpty()) searchResultsTvAdapter.setData(it, genres, this)
                showTextResult()
                hideLoading()
            })
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_search, menu)
        menu?.let {
            it.findItem(R.id.search).expandActionView()
            searchView = (it.findItem(R.id.search).actionView as SearchView).apply {
                setSearchableInfo((getSystemService(Context.SEARCH_SERVICE) as SearchManager).getSearchableInfo(componentName))
                setIconifiedByDefault(true)
                setOnQueryTextListener(this@SearchResultsActivity)
                queryHint =
                    if(contentType == ContentType.MOVIE)
                        getString(R.string.search_input_hint_movies)
                    else
                        getString(R.string.search_input_hint_tv)
            }
            searchView?.setQuery(query, true)
            searchView?.clearFocus()
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchView?.clearFocus()
        searchViewModel.loaded = false
        if(!query.isNullOrEmpty()) refreshSearchList(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean = true

    private fun refreshSearchList(query: String) {
        if(!searchViewModel.loaded) {
            showLoading()
            hideTextResult()
        }
        this.query = query
        when(contentType) {
            ContentType.MOVIE -> searchViewModel.searchMovies(query = query)
            ContentType.TV -> searchViewModel.searchTvs(query = query)
        }
    }

    override fun onResume() {
        super.onResume()
        searchView?.clearFocus()
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
    }

    private fun showTextResult() {
        textSearchResult.apply {
            visibility = View.VISIBLE
            text = utilsRepository.formatHtmlFromString(
                getString(
                    R.string.text_search_result_activity_search_results,
                    query
                )
            )
        }
    }

    private fun hideTextResult() {
        textSearchResult.visibility = View.GONE
    }

    private fun showLoading() {
        loading.playAnimation()
        loading.visibility = View.VISIBLE
        recyclerView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        loading.pauseAnimation()
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
}
