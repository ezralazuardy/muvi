package com.muvi.view.adapter.tv

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
import com.muvi.model.discover.DiscoverTvListResult
import com.muvi.model.genre.Genre
import kotlinx.android.synthetic.main.item_list_fragment_activity_list.view.*
import java.util.*

class TvListAdapter : RecyclerView.Adapter<TvListAdapter.ViewHolder>() {

    companion object {
        const val type: String = "TV"
    }

    private val tvs: ArrayList<DiscoverTvListResult> = ArrayList()
    private val tvGenreList: ArrayList<Genre> = ArrayList()
    private lateinit var onTvClickListener: OnTvClickListener

    fun setData(
        tvs: List<DiscoverTvListResult>,
        tvGenreList: List<Genre>,
        onTvClickListener: OnTvClickListener
    ) {
        this.tvs.clear()
        this.tvs.addAll(tvs)
        this.tvGenreList.clear()
        this.tvGenreList.addAll(tvGenreList)
        this.onTvClickListener = onTvClickListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_fragment_activity_list,
            parent,
            false
        ), onTvClickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (tvs[position].poster_path != null) holder.bind(tvs[position], tvGenreList)
    }

    override fun getItemCount(): Int = tvs.size

    class ViewHolder(itemView: View, private val onTvClickListener: OnTvClickListener) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(tv: DiscoverTvListResult, genres: List<Genre>) {
            Glide.with(itemView.context)
                .load(AppConfig.TMDB_API_IMAGE_BASE_URL + tv.poster_path)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                .transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.background_poster_item_list)
                .error(R.drawable.background_poster_item_list)
                .into(itemView.coverImageView)
            itemView.itemTitleTextViewItemList.text = tv.original_name
            itemView.ratingChipItemList.text = tv.vote_average.toString()
            itemView.languageChipItemList.text =
                tv.original_language.toUpperCase(Locale.getDefault())
            itemView.chipGroup2ItemList.removeAllViews()
            var chipGenreCount = 0
            for (tvGenre in tv.genre_ids) {
                for (genre in genres) {
                    if (tvGenre == genre.id && chipGenreCount <= 1) {
                        addGenreChip(genre.name)
                        chipGenreCount++
                    }
                }
            }
            itemView.itemCardView.setOnClickListener { onTvClickListener.onTvItemClick(tv) }
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