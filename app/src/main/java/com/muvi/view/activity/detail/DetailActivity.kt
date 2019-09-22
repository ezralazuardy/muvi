package com.muvi.view.activity.detail

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
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.view.activity.list.ListActivityViewPagerAdapter
import com.muvi.view.adapter.movie.MovieListAdapter
import com.muvi.view.adapter.tv.TvListAdapter
import com.muvi.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.error
import java.util.*

class DetailActivity : AppCompatActivity(), AnkoLogger {

    private var activityType: Int = 1
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        initViewModel()
        getData()
    }

    private fun initViewModel() {
        detailViewModel =
            ViewModelProviders.of(this@DetailActivity).get(DetailViewModel::class.java)
    }

    private fun getData() {
        val type: String? = intent.getStringExtra("type")
        if (type == null) {
            error { "No type defined in Detail Activity" }
            finish()
        } else {
            when (type) {
                MovieListAdapter.type -> {
                    val movie: DiscoverMovieListResult? = intent.getParcelableExtra("movie")
                    if (movie == null) {
                        error { "No movie data defined in Detail Activity" }
                        finish()
                    } else {
                        setMovieComponent(movie)
                    }
                }
                TvListAdapter.type -> {
                    activityType = 2
                    val tv: DiscoverTvListResult? = intent.getParcelableExtra("tv")
                    if (tv == null) {
                        error { "No tv data defined in Detail Activity" }
                        finish()
                    } else {
                        setTvComponent(tv)
                    }
                }
            }
        }
    }

    private fun setMovieComponent(movie: DiscoverMovieListResult) {
        if (!detailViewModel.isLoaded()) showLoading()
        detailViewModel.apply {
            getDetail(type = 1, id = movie.id.toString())
        }
        detailViewModel.movieDetail.observe(this@DetailActivity, Observer {
            detailViewModel.setLoaded()
            if (it == null) {
                showError("EMPTY MOVIE DETAIL")
            } else {
                Glide.with(this@DetailActivity)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imageBackdropActivityDetail)
                Glide.with(this@DetailActivity)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.poster_path)
                    .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imagePosterActivityDetail)

                titleActivityDetail.text = it.original_title
                shortTitleActivityDetail.text = it.original_title
                for (genre in it.genres) {
                    addGenreChip(genre.name)
                }
                ratingActivityDetail.text = resources.getString(
                    R.string.rating_value_activity_detail,
                    it.vote_average.toString()
                )
                votingActivityDetail.text = it.vote_count.toString()
                languageActivityDetail.text = it.original_language.toUpperCase(Locale.getDefault())
                releaseDateActivityDetail.text = it.release_date
                statusActivityDetail.text = it.status
                overviewActivityDetail.text = it.overview.trim()

                var productionCompany = ""
                for (company in it.production_companies) {
                    productionCompany += "${company.name.trim()}, "
                }
                if (productionCompany.isNotEmpty()) productionCompanyActivityDetail.text =
                    productionCompany.substring(0, productionCompany.length - 2)

                val icon: Drawable? = ContextCompat.getDrawable(
                    this@DetailActivity,
                    ListActivityViewPagerAdapter.tabIcons[0]
                )
                icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                activityTypeChipActivityDetail.chipIcon = icon
                activityTypeChipActivityDetail.text =
                    resources.getString(R.string.tab_text_1_activity_list)

                hideLoading()
            }
        })
    }

    private fun setTvComponent(tv: DiscoverTvListResult) {
        if (!detailViewModel.isLoaded()) showLoading()
        detailViewModel.apply {
            getDetail(type = 2, id = tv.id.toString())
        }
        detailViewModel.tvDetail.observe(this@DetailActivity, Observer {
            detailViewModel.setLoaded()
            if (it == null) {
                showError("EMPTY TV DETAIL")
            } else {
                Glide.with(this@DetailActivity)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.backdrop_path)
                    .apply(RequestOptions().transform(CenterCrop()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imageBackdropActivityDetail)
                Glide.with(this@DetailActivity)
                    .load(AppConfig.TMDB_API_IMAGE_BASE_URL + it.poster_path)
                    .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.background_poster_item_list)
                    .error(R.drawable.background_poster_item_list)
                    .into(imagePosterActivityDetail)

                titleActivityDetail.text = it.original_name
                shortTitleActivityDetail.text = it.original_name
                for (genre in it.genres) {
                    addGenreChip(genre.name)
                }
                ratingActivityDetail.text = resources.getString(
                    R.string.rating_value_activity_detail,
                    it.vote_average.toString()
                )
                votingActivityDetail.text = it.vote_count.toString()
                languageActivityDetail.text = it.original_language.toUpperCase(Locale.getDefault())
                releaseDateActivityDetail.text = it.first_air_date
                statusActivityDetail.text = it.status
                overviewActivityDetail.text = it.overview.trim()

                var productionCompany = ""
                for (company in it.production_companies) {
                    productionCompany += "${company.name.trim()}, "
                }
                if (productionCompany.isNotEmpty()) {
                    productionCompanyActivityDetail.text =
                        productionCompany.substring(0, productionCompany.length - 2)
                } else {
                    productionCompanyTextActivityDetail.visibility = View.GONE
                }

                val icon: Drawable? = ContextCompat.getDrawable(
                    this@DetailActivity,
                    ListActivityViewPagerAdapter.tabIcons[1]
                )
                icon?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
                activityTypeChipActivityDetail.chipIcon = icon
                activityTypeChipActivityDetail.text =
                    resources.getString(R.string.tab_text_2_activity_list)

                hideLoading()
            }
        })
    }

    private fun showError(log: String) {
        error { log }
    }

    private fun showLoading() {
        if (!loadingActivityDetail.isAnimating) loadingActivityDetail.playAnimation()
        loadingFrameActivityDetail.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loadingFrameActivityDetail.visibility = View.GONE
        if (loadingActivityDetail.isAnimating) loadingActivityDetail.pauseAnimation()
    }

    private fun addGenreChip(name: String) {
        val chip = Chip(ContextThemeWrapper(this@DetailActivity, R.style.EntryChipMaterialStyle))
        chip.text = name
        chip.stateListAnimator = null
        chip.isCheckable = false
        chip.isClickable = false
        chip.isFocusable = false
        chip.closeIcon = null
        chip.maxLines = 1
        chip.ellipsize = TextUtils.TruncateAt.END
        chip.chipBackgroundColor =
            ContextCompat.getColorStateList(this@DetailActivity, R.color.colorChip)
        chip.setTextAppearance(R.style.ChipTextSmallStyle)
        chip.setTextColor(ContextCompat.getColor(this@DetailActivity, R.color.colorTextPrimary))
        chipGroupGenreActivityDetail.addView(chip)
    }

    private fun setListener() {
        backButton.setOnClickListener { finish() }
    }

    private fun unsetListener() {
        backButton.setOnClickListener(null)
    }

    override fun onPause() {
        super.onPause()
        loadingActivityDetail.pauseAnimation()
    }

    override fun onStart() {
        super.onStart()
        setListener()
    }

    override fun onStop() {
        super.onStop()
        unsetListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        when (activityType) {
            1 -> detailViewModel.disposeRequestMovie()
            2 -> detailViewModel.disposeRequestTv()
        }
    }
}