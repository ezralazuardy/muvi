package com.muvi.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muvi.api.ApiUtils
import com.muvi.model.detail.Movie
import com.muvi.model.detail.Tv
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error

class DetailViewModel : ViewModel(), AnkoLogger {

    private val tmdbService = ApiUtils.getTMDBApiService()
    private val compositeDisposableMovie = CompositeDisposable()
    private val compositeDisposableTv = CompositeDisposable()
    private val _movieDetail = MutableLiveData<Movie>()
    private val _tvDetail = MutableLiveData<Tv>()
    private var isLoaded: Boolean = false
    val movieDetail: LiveData<Movie> = _movieDetail
    val tvDetail: LiveData<Tv> = _tvDetail

    fun isLoaded() = isLoaded

    fun setLoaded(state: Boolean = true) {
        isLoaded = state
    }

    fun getDetail(type: Int = 1, id: String = "") {
        when (type) {
            1 -> compositeDisposableMovie.add(
                tmdbService.getMovieDetail(movieId = id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        _movieDetail.postValue(it)
                    }, {
                        _movieDetail.postValue(null)
                        error { "ERROR | REQUEST MOVIE DETAIL | ${it.message!!}" }
                    })
            )
            2 -> compositeDisposableTv.add(
                tmdbService.getTvDetail(tvId = id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .subscribe({
                        _tvDetail.postValue(it)
                    }, {
                        _tvDetail.postValue(null)
                        error { "ERROR | REQUEST TV DETAIL | ${it.message!!}" }
                    })
            )
        }
    }

    fun disposeRequestMovie() = compositeDisposableMovie.dispose()

    fun disposeRequestTv() = compositeDisposableTv.dispose()
}