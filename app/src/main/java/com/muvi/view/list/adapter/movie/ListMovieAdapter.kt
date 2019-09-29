package com.muvi.view.list.adapter.movie

import android.text.TextUtils
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.model.discover.DiscoverMovieListResult
import com.muvi.model.genre.Genre
import kotlinx.android.synthetic.main.item_list_fragment_activity_list.view.*
import java.util.*

class ListMovieAdapter : RecyclerView.Adapter<ListMovieAdapter.ViewHolder>() {

    private val movies: ArrayList<DiscoverMovieListResult> = ArrayList()
    private val movieGenreList: ArrayList<Genre> = ArrayList()
    private lateinit var onListMovieClickListener: OnListMovieClickListener

    fun setData(
        movies: List<DiscoverMovieListResult>,
        movieGenreList: List<Genre>,
        onListMovieClickListener: OnListMovieClickListener
    ) {
        this.movies.clear()
        this.movies.addAll(movies)
        this.movieGenreList.clear()
        this.movieGenreList.addAll(movieGenreList)
        this.onListMovieClickListener = onListMovieClickListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_fragment_activity_list,
            parent,
            false
        ), onListMovieClickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(movies[position], movieGenreList)
    }

    override fun getItemCount(): Int = movies.size

    class ViewHolder(itemView: View, private val onMovieClickListener: OnListMovieClickListener) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(movie: DiscoverMovieListResult, genres: List<Genre>) {
            Glide.with(itemView.context)
                .load(AppConfig.TMDB_API_IMAGE_BASE_URL + movie.poster_path)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.background_poster_item_list)
                .error(R.drawable.background_poster_item_list)
                .into(itemView.coverImageView)
            itemView.itemTitleTextViewItemList.text = movie.original_title
            itemView.ratingChipItemList.text = movie.vote_average.toString()
            itemView.languageChipItemList.text =
                movie.original_language.toUpperCase(Locale.getDefault())
            itemView.chipGroup2ItemList.removeAllViews()
            var chipGenreCount = 0
            for (movieGenre in movie.genre_ids) {
                for (genre in genres) {
                    if (movieGenre == genre.id && chipGenreCount <= 1) {
                        if (genre.name != null) addGenreChip(genre.name)
                        chipGenreCount++
                    }
                }
            }
            itemView.itemCardView.setOnClickListener { onMovieClickListener.onMovieItemClick(movie) }
        }

        private fun addGenreChip(name: String) {
            val chip = Chip(ContextThemeWrapper(itemView.context, R.style.EntryChipMaterialStyle))
            chip.text = name
            chip.stateListAnimator = null
            chip.isCheckable = false
            chip.isClickable = false
            chip.isFocusable = false
            chip.closeIcon = null
            chip.maxLines = 1
            chip.ellipsize = TextUtils.TruncateAt.END
            chip.chipBackgroundColor =
                ContextCompat.getColorStateList(itemView.context, R.color.colorChip)
            chip.setTextAppearance(R.style.ChipTextSmallStyle)
            chip.setTextColor(ContextCompat.getColor(itemView.context, R.color.colorTextPrimary))
            itemView.chipGroup2ItemList.addView(chip)
        }
    }
}