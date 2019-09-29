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
import androidx.lifecycle.ViewModelProvider
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
import com.muvi.view.list.ListActivity
import com.muvi.view.list.ListActivityViewPagerAdapter
import com.muvi.viewmodel.detail.DetailViewModel
import com.muvi.viewmodel.detail.DetailViewModelFactory
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.error
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class DetailActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var detailViewModel: DetailViewModel
    private lateinit var movie: Movie
    private lateinit var tv: Tv
    private var contentType: ContentType = ContentType.MOVIE
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        getData()
    }

    private fun getData() {
        val type = intent.getSerializableExtra("type")
        if (type == null) {
            error { "No type defined in Detail Activity" }
            finish()
        } else {
            var id = 0
            when (type) {
                ContentType.MOVIE -> {
                    if (intent.getStringExtra("openFrom") == ListActivity::class.java.simpleName) {
                        val movie: DiscoverMovieListResult? = intent.getParcelableExtra("movie")
                        if (movie == null) {
                            error { "No movie data defined in Detail Activity" }
                            finish()
                        } else {
                            id = movie.id
                        }
                    } else {
                        val movie: MovieEntity? = intent.getParcelableExtra("movie")
                        if (movie == null) {
                            error { "No movie data defined in Detail Activity" }
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        } else {
                            id = movie.id
                        }
                    }
                    setMovieDetailComponent(id)
                }
                ContentType.TV -> {
                    contentType = ContentType.TV
                    if (intent.getStringExtra("openFrom") == ListActivity::class.java.simpleName) {
                        val tv: DiscoverTvListResult? = intent.getParcelableExtra("tv")
                        if (tv == null) {
                            error { "No tv data defined in Detail Activity" }
                            finish()
                        } else {
                            id = tv.id
                        }
                    } else {
                        val tv: TvEntity? = intent.getParcelableExtra("tv")
                        if (tv == null) {
                            error { "No tv data defined in Detail Activity" }
                            setResult(Activity.RESULT_CANCELED)
                            finish()
                        } else {
                            id = tv.id
                        }
                    }
                    setTvDetailComponent(id)
                }
                else -> finish()
            }
        }
    }

    private fun setMovieDetailComponent(id: Int) {
        detailViewModel =
            ViewModelProvider(
                this,
                DetailViewModelFactory(application, id)
            ).get(DetailViewModel::class.java)
        if (detailViewModel.loaded) hideLoading() else showLoading()
        detailViewModel.movieDetail.observe(this, Observer {
            detailViewModel.loaded = true
            if (it == null) {
                showError("EMPTY MOVIE DETAIL")
            } else {
                this.movie = it
                Glide.with(this)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imageBackdrop)
                Glide.with(this)
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
                releaseDateText.text = it.release_date
                statusText.text = it.status
                overviewText.text = it.overview.trim()

                var productionCompany = ""
                for (company in it.production_companies) productionCompany += "${company.name.trim()}, "
                if (productionCompany.isNotEmpty())
                    productionCompanyText.text =
                        productionCompany.substring(0, productionCompany.length - 2)
                else
                    productionCompanyText.visibility = View.GONE

                val icon: Drawable? = ContextCompat.getDrawable(
                    this,
                    ListActivityViewPagerAdapter.tabIcons[0]
                )
                icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                activityTypeChip.chipIcon = icon
                activityTypeChip.text =
                    resources.getString(R.string.tab_text_1_activity_list)

                checkIsFavorite(movie.id, ContentType.MOVIE)
                hideLoading()
            }
        })
    }

    private fun setTvDetailComponent(id: Int) {
        detailViewModel =
            ViewModelProvider(
                this,
                DetailViewModelFactory(application, id)
            ).get(DetailViewModel::class.java)
        if (detailViewModel.loaded) hideLoading() else showLoading()
        detailViewModel.tvDetail.observe(this, Observer {
            detailViewModel.loaded = true
            if (it == null) {
                showError("EMPTY TV DETAIL")
            } else {
                this.tv = it
                Glide.with(this)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imageBackdrop)
                Glide.with(this)
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
                releaseDateText.text = it.first_air_date
                statusText.text = it.status
                overviewText.text = it.overview.trim()

                var productionCompany = ""
                for (company in it.production_companies) productionCompany += "${company.name.trim()}, "
                if (productionCompany.isNotEmpty())
                    productionCompanyText.text =
                        productionCompany.substring(0, productionCompany.length - 2)
                else
                    productionCompanyText.visibility = View.GONE

                val icon: Drawable? = ContextCompat.getDrawable(
                    this,
                    ListActivityViewPagerAdapter.tabIcons[1]
                )
                icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                activityTypeChip.chipIcon = icon
                activityTypeChip.text =
                    resources.getString(R.string.tab_text_2_activity_list)

                checkIsFavorite(tv.id, ContentType.TV)
                hideLoading()
            }
        })
    }

    private fun checkIsFavorite(id: Int, type: Any) {
        when (type) {
            ContentType.MOVIE -> {
                detailViewModel.checkIsFavouriteMovie(id).observe(this, Observer {
                    it?.let {
                        isFavorite = true
                        favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
                    }
                })
            }
            ContentType.TV -> {
                detailViewModel.checkIsFavouriteTv(id).observe(this, Observer {
                    it?.let {
                        isFavorite = true
                        favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
                    }
                })
            }
        }
    }

    private fun addMovieToFavourite(movieEntity: MovieEntity) {
        detailViewModel.addMovieToFavourite(movieEntity).observe(this, Observer {
            if (it != null) {
                isFavorite = true
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
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
                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
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
                                favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
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
                            favouriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_white))
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