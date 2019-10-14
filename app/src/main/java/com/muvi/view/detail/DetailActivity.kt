/*
 * Created by Ezra Lazuardy on 10/14/19 9:55 AM
 * Copyright (c) 2019 . All rights reserved.
 * Last modified 10/14/19 9:54 AM
 */

package com.muvi.view.detail

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.config.ContentType
import com.muvi.database.local.entity.MovieEntity
import com.muvi.database.local.entity.TvEntity
import com.muvi.model.detail.Movie
import com.muvi.model.detail.Tv
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.view.favourite.FavouriteActivity
import com.muvi.view.list.adapter.viewpager.ListActivityViewPagerAdapter
import com.muvi.viewmodel.detail.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DetailActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var movie: Movie
    private lateinit var tv: Tv
    private var contentType: ContentType = ContentType.MOVIE
    private var isFavorite = false
    private val detailViewModel: DetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        getData()
    }

    private fun getData() {
        val type = intent.getSerializableExtra(AppConfig.INTENT_EXTRA_CONTENT_TYPE)
        val openFrom = intent.getStringExtra(AppConfig.INTENT_EXTRA_OPEN_FROM)
        var id = 0
        when (type) {
            ContentType.MOVIE -> {
                when(openFrom) {
                    FavouriteActivity::class.java.simpleName -> {
                        val movie: MovieEntity? = intent.getParcelableExtra(AppConfig.INTENT_EXTRA_DATA_MOVIE)
                        if (movie == null) {
                            error { "No movie data defined in Detail Activity" }
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        } else {
                            id = movie.id
                        }
                    }
                    else -> {
                        val movie: DiscoverMovieListResult? = intent.getParcelableExtra(AppConfig.INTENT_EXTRA_DATA_MOVIE)
                        if (movie == null) {
                            error { "No movie data defined in Detail Activity" }
                            finish()
                        } else {
                            id = movie.id
                        }

                    }
                }
                setMovieDetailComponent(id)
            }
            ContentType.TV -> {
                contentType = ContentType.TV
                when(openFrom) {
                    FavouriteActivity::class.java.simpleName -> {
                        val tv: TvEntity? = intent.getParcelableExtra(AppConfig.INTENT_EXTRA_DATA_TV)
                        if (tv == null) {
                            error { "No tv data defined in Detail Activity" }
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        } else {
                            id = tv.id
                        }
                    }
                    else -> {
                        val tv: DiscoverTvListResult? = intent.getParcelableExtra(AppConfig.INTENT_EXTRA_DATA_TV)
                        if (tv == null) {
                            error { "No tv data defined in Detail Activity" }
                            finish()
                        } else {
                            id = tv.id
                        }
                    }
                }
                setTvDetailComponent(id)
            }
            else -> {
                error { "No type defined in Detail Activity" }
                finish()
            }
        }
    }

    private fun setMovieDetailComponent(id: Int) {
        with(detailViewModel) {
            contentId = id
            if(loaded) hideLoading() else showLoading()
            movieDetail.observe(this@DetailActivity, Observer {
                loaded = true
                it?.let {
                    this@DetailActivity.movie = it
                    Glide.with(this@DetailActivity)
                        .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                        .apply(RequestOptions().transform(CenterCrop()))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.background_poster_item_list)
                        .error(R.drawable.background_poster_item_list)
                        .into(imageBackdrop)
                    Glide.with(this@DetailActivity)
                        .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.poster_path)
                        .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.background_poster_item_list)
                        .error(R.drawable.background_poster_item_list)
                        .into(imagePoster)

                    titleText.text = it.original_title
                    shortTitleText.text = it.original_title
                    for (genre in it.genres) addGenreChip(genre.name)
                    ratingText.text = resources.getString(
                        R.string.rating_value_activity_detail,
                        it.vote_average.toString()
                    )
                    votingText.text = it.vote_count.toString()
                    languageText.text = it.original_language.toUpperCase(Locale.getDefault())

                    if(it.release_date.isNotEmpty()) {
                        releaseDateText.text = detailViewModel.getFormattedDate(it.release_date)
                    } else {
                        releaseDateTextName.visibility = View.GONE
                        releaseDateText.visibility = View.GONE
                    }

                    if(it.status.isNotEmpty()) {
                        statusText.text = it.status
                    } else {
                        statusTextName.visibility = View.GONE
                        statusText.visibility = View.GONE
                    }

                    var productionCompany = ""
                    for (company in it.production_companies) productionCompany += "${company.name.trim()}, "
                    if (it.production_companies.isNotEmpty()) {
                        productionCompanyText.text =
                            productionCompany.substring(0, productionCompany.length - 2)
                    } else {
                        productionCompanyTextName.visibility = View.GONE
                        productionCompanyText.visibility = View.GONE
                    }

                    if(it.overview.isNotEmpty()) {
                        overviewText.text = it.overview.trim()
                    } else {
                        overviewTextName.visibility = View.GONE
                        overviewText.visibility = View.GONE
                    }

                    val icon: Drawable? = ContextCompat.getDrawable(
                        this@DetailActivity,
                        ListActivityViewPagerAdapter.tabIcons[0]
                    )
                    icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    activityTypeChip.chipIcon = icon
                    activityTypeChip.text =
                        resources.getString(R.string.tab_text_1_activity_list)
                    checkIsFavorite(movie.id, ContentType.MOVIE)
                    hideLoading()
                } ?: showError("EMPTY MOVIE DETAIL")
            })
        }
    }

    private fun setTvDetailComponent(id: Int) {
        with(detailViewModel) {
            contentId = id
            if(loaded) hideLoading() else showLoading()
            tvDetail.observe(this@DetailActivity, Observer {
                detailViewModel.loaded = true
                it?.let {
                    this@DetailActivity.tv = it
                    Glide.with(this@DetailActivity)
                        .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                        .apply(RequestOptions().transform(CenterCrop()))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.background_poster_item_list)
                        .error(R.drawable.background_poster_item_list)
                        .into(imageBackdrop)
                    Glide.with(this@DetailActivity)
                        .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.poster_path)
                        .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.background_poster_item_list)
                        .error(R.drawable.background_poster_item_list)
                        .into(imagePoster)

                    titleText.text = it.original_name
                    shortTitleText.text = it.original_name
                    for (genre in it.genres) addGenreChip(genre.name)
                    ratingText.text = resources.getString(
                        R.string.rating_value_activity_detail,
                        it.vote_average.toString()
                    )
                    votingText.text = it.vote_count.toString()
                    languageText.text = it.original_language.toUpperCase(Locale.getDefault())


                    if(it.first_air_date.isNotEmpty()) {
                        releaseDateText.text = detailViewModel.getFormattedDate(it.first_air_date)
                    } else {
                        releaseDateTextName.visibility = View.GONE
                        releaseDateText.visibility = View.GONE
                    }

                    if(it.status.isNotEmpty()) {
                        statusText.text = it.status
                    } else {
                        statusTextName.visibility = View.GONE
                        statusText.visibility = View.GONE
                    }

                    var productionCompany = ""
                    for (company in it.production_companies) productionCompany += "${company.name.trim()}, "
                    if (it.production_companies.isNotEmpty()) {
                        productionCompanyText.text =
                            productionCompany.substring(0, productionCompany.length - 2)
                    } else {
                        productionCompanyTextName.visibility = View.GONE
                        productionCompanyText.visibility = View.GONE
                    }

                    if(it.overview.isNotEmpty()) {
                        overviewText.text = it.overview.trim()
                    } else {
                        overviewTextName.visibility = View.GONE
                        overviewText.visibility = View.GONE
                    }

                    val icon: Drawable? = ContextCompat.getDrawable(
                        this@DetailActivity,
                        ListActivityViewPagerAdapter.tabIcons[1]
                    )
                    icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                    activityTypeChip.chipIcon = icon
                    activityTypeChip.text =
                        resources.getString(R.string.tab_text_2_activity_list)
                    checkIsFavorite(tv.id, ContentType.TV)
                    hideLoading()
                } ?: showError("EMPTY TV DETAIL")
            })
        }
    }

    private fun checkIsFavorite(id: Int, type: Any) {
        when (type) {
            ContentType.MOVIE -> {
                detailViewModel.checkIsFavouriteMovie(id).observe(this, Observer {
                    it?.let {
                        isFavorite = true
                        favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                    }
                })
            }
            ContentType.TV -> {
                detailViewModel.checkIsFavouriteTv(id).observe(this, Observer {
                    it?.let {
                        isFavorite = true
                        favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                    }
                })
            }
        }
    }

    private fun addMovieToFavourite(movieEntity: MovieEntity) {
        detailViewModel.addMovieToFavourite(movieEntity).observe(this, Observer {
            if (it != null) {
                isFavorite = true
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_added_to_favourite),
                    getString(R.string.snackbar_button_undo)
                ) {
                    detailViewModel.removeMovieFromFavourite(movieEntity)
                        .observe(this, Observer { response ->
                            if (response > 0) {
                                isFavorite = false
                                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white))
                            } else {
                                layoutActivityDetail.snackbar(getString(R.string.snackbar_unknown_error))
                            }
                        })
                }
            } else {
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_unknown_error),
                    getString(R.string.snackbar_retry_button)
                ) { addMovieToFavourite(movieEntity) }
            }
        })
    }

    private fun addTvToFavourite(tvEntity: TvEntity) {
        detailViewModel.addTvToFavourite(tvEntity).observe(this, Observer {
            if (it != null) {
                isFavorite = true
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_added_to_favourite),
                    getString(R.string.snackbar_button_undo)
                ) {
                    detailViewModel.removeTvFromFavourite(tvEntity)
                        .observe(this, Observer { response ->
                            if (response > 0) {
                                isFavorite = false
                                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white))
                            } else {
                                layoutActivityDetail.snackbar(getString(R.string.snackbar_unknown_error))
                            }
                        })
                }
            } else {
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_unknown_error),
                    getString(R.string.snackbar_retry_button)
                ) { addTvToFavourite(tvEntity) }
            }
        })
    }

    private fun removeMovieFromFavourite(movieEntity: MovieEntity) {
        detailViewModel.removeMovieFromFavourite(movieEntity).observe(this, Observer {
            if (it > 0) {
                isFavorite = false
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white))
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_deleted_from_favourite),
                    getString(R.string.snackbar_button_undo)
                ) {
                    detailViewModel.addMovieToFavourite(movieEntity)
                        .observe(this, Observer { response ->
                            if (response != null) {
                                isFavorite = true
                                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                            } else {
                                layoutActivityDetail.snackbar(getString(R.string.snackbar_unknown_error))
                            }
                        })
                }
            } else {
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_unknown_error),
                    getString(R.string.snackbar_retry_button)
                ) { removeMovieFromFavourite(movieEntity) }
            }
        })
    }

    private fun removeTvFromFavourite(tvEntity: TvEntity) {
        detailViewModel.removeTvFromFavourite(tvEntity).observe(this, Observer {
            if (it > 0) {
                isFavorite = false
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_white))
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_deleted_from_favourite),
                    getString(R.string.snackbar_button_undo)
                ) {
                    detailViewModel.addTvToFavourite(tvEntity).observe(this, Observer { response ->
                        if (response != null) {
                            isFavorite = true
                            favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_accent))
                        } else {
                            layoutActivityDetail.snackbar(getString(R.string.snackbar_unknown_error))
                        }
                    })
                }
            } else {
                layoutActivityDetail.snackbar(
                    getString(R.string.snackbar_unknown_error),
                    getString(R.string.snackbar_retry_button)
                ) { removeTvFromFavourite(tvEntity) }
            }
        })
    }

    private fun showError(log: String) {
        error { log }
    }

    private fun showLoading() {
        if (!loading.isAnimating) loading.playAnimation()
        loadingFrame.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingFrame.visibility = View.GONE
        if (loading.isAnimating) loading.pauseAnimation()
    }

    private fun addGenreChip(name: String) {
        val chip = Chip(ContextThemeWrapper(this, R.style.EntryChipMaterialStyle))
        chip.text = name
        chip.stateListAnimator = null
        chip.isCheckable = false
        chip.isClickable = false
        chip.isFocusable = false
        chip.closeIcon = null
        chip.maxLines = 1
        chip.ellipsize = TextUtils.TruncateAt.END
        chip.chipBackgroundColor =
            ContextCompat.getColorStateList(this, R.color.colorChip)
        chip.setTextAppearance(R.style.ChipTextSmallStyle)
        chip.setTextColor(ContextCompat.getColor(this, R.color.colorTextPrimary))
        chipGroupGenre.addView(chip)
    }

    private fun setListener() {
        backButton.onClick {
            onBackPressed()
        }
        favouriteButton.onClick {
            if (detailViewModel.loaded) {
                when (contentType) {
                    ContentType.MOVIE -> {
                        var genres = ""
                        for (genre in movie.genres) genres += "${genre.name.trim()}, "
                        genres.substring(0, genres.length - 2)
                        if (isFavorite) {
                            removeMovieFromFavourite(
                                MovieEntity(
                                    movie.id,
                                    movie.poster_path,
                                    movie.title,
                                    movie.vote_average,
                                    movie.original_language,
                                    genres
                                )
                            )
                        } else {
                            addMovieToFavourite(
                                MovieEntity(
                                    movie.id,
                                    movie.poster_path,
                                    movie.title,
                                    movie.vote_average,
                                    movie.original_language,
                                    genres
                                )
                            )
                        }
                    }
                    ContentType.TV -> {
                        var genres = ""
                        for (genre in tv.genres) genres += "${genre.name.trim()}, "
                        genres.substring(0, genres.length - 2)
                        if (isFavorite) {
                            removeTvFromFavourite(
                                TvEntity(
                                    tv.id,
                                    tv.poster_path,
                                    tv.original_name,
                                    tv.vote_average,
                                    tv.original_language,
                                    genres
                                )
                            )
                        } else {
                            addTvToFavourite(
                                TvEntity(
                                    tv.id,
                                    tv.poster_path,
                                    tv.original_name,
                                    tv.vote_average,
                                    tv.original_language,
                                    genres
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun unsetListener() {
        backButton.setOnClickListener(null)
        favouriteButton.setOnClickListener(null)
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    override fun onPause() {
        super.onPause()
        loading.pauseAnimation()
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onStop() {
        super.onStop()
        unsetListener()
    }
}