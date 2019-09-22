package com.muvi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muvi.api.ApiUtils
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class ListViewModel : ViewModel(), AnkoLogger {

    private val tmdbService = ApiUtils.getTMDBApiService()
    private val compositeDisposableMovie = CompositeDisposable()
    private val compositeDisposableTv = CompositeDisposable()
    private val _index = MutableLiveData<Int>()
    private var _movieGenreList = MutableLiveData<List<Genre>>()
    private var _tvGenreList = MutableLiveData<List<Genre>>()
    private var _discoverMovieList = MutableLiveData<List<DiscoverMovieListResult>>()
    private var _discoverTvList = MutableLiveData<List<DiscoverTvListResult>>()
    private var isLoaded: Boolean = false
    val movieGenreList: LiveData<List<Genre>> = _movieGenreList
    val tvGenreList: LiveData<List<Genre>> = _tvGenreList
    val discoverMovieList: LiveData<List<DiscoverMovieListResult>> = _discoverMovieList
    val discoverTvList: LiveData<List<DiscoverTvListResult>> = _discoverTvList

    fun isLoaded() = isLoaded

    fun setLoaded(state: Boolean = true) {
        isLoaded = state
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    fun requestGenreList(type: Int = 1) {
        when (type) {
            1 -> compositeDisposableMovie.add(
                tmdbService.getMovieGenreList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        _movieGenreList.postValue(it.genres)
                    }, {
                        _movieGenreList.postValue(null)
                        error { "ERROR | REQUEST MOVIE GENRE LIST | ${it.message!!}" }
                    })
            )
            2 -> compositeDisposableTv.add(
                tmdbService.getTvGenreList()
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        _tvGenreList.postValue(it.genres)
                    }, {
                        _tvGenreList.postValue(null)
                        error { "ERROR | REQUEST TV GENRE LIST | ${it.message!!}" }
                    })
            )
        }
    }

    fun requestDiscover(type: Int = 1, page: Int = 1) {
        when (type) {
            1 -> compositeDisposableMovie.add(
                tmdbService.discoverMovies(page = page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        val result = mutableListOf<DiscoverMovieListResult>()
                        for (movie in it.results) if (!movie.poster_path.isNullOrEmpty()) result.add(
                            movie
                        )
                        _discoverMovieList.postValue(result)
                    }, {
                        _discoverMovieList.postValue(null)
                        error { "ERROR | REQUEST DISCOVER MOVIE | ${it.message!!}" }
                    })
            )
            2 -> compositeDisposableTv.add(
                tmdbService.discoverTvs(page = page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        val result = mutableListOf<DiscoverTvListResult>()
                        for (tv in it.results) if (!tv.poster_path.isNullOrEmpty()) result.add(tv)
                        _discoverTvList.postValue(result)
                    }, {
                        _discoverTvList.postValue(null)
                        error { "ERROR | REQUEST DISCOVER TV | ${it.message!!}" }
                    })
            )
        }
    }

    fun disposeRequestDiscoverMovies() = compositeDisposableMovie.dispose()

    fun disposeRequestDiscoverTvs() = compositeDisposableTv.dispose()
}