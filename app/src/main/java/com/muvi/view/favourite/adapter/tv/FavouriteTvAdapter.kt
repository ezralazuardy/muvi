package com.muvi.view.favourite.adapter.tv

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.chip.Chip
import com.muvi.R
import com.muvi.config.AppConfig
import com.muvi.database.local.entity.TvEntity
import kotlinx.android.synthetic.main.item_list_fragment_activity_list.view.*
import java.util.*
import kotlin.collections.ArrayList

class FavouriteTvAdapter : RecyclerView.Adapter<FavouriteTvAdapter.ViewHolder>() {

    private val tvEntities: ArrayList<TvEntity> by lazy { ArrayList<TvEntity>() }
    private lateinit var onFavouriteTvClickListener: OnFavouriteTvClickListener

    fun setData(
        tvEntities: List<TvEntity>,
        onFavouriteTvClickListener: OnFavouriteTvClickListener
    ) {
        this.tvEntities.clear()
        this.tvEntities.addAll(tvEntities)
        this.onFavouriteTvClickListener = onFavouriteTvClickListener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_list_fragment_activity_favourite,
            parent,
            false
        ), onFavouriteTvClickListener
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(tvEntities[position])
    }

    override fun getItemCount(): Int = tvEntities.size

    class ViewHolder(
        itemView: View,
        private val onFavouriteTvClickListener: OnFavouriteTvClickListener
    ) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(tvEntity: TvEntity) {
            Glide.with(itemView.context)
                .load(AppConfig.TMDB_API_IMAGE_BASE_URL + tvEntity.poster_path)
                .apply(RequestOptions().transform(CenterCrop()).transform(RoundedCorners(14)))
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.background_poster_item_list)
                .error(R.drawable.background_poster_item_list)
                .into(itemView.coverImageView)
            itemView.itemTitleTextViewItemList.text = tvEntity.title
            itemView.ratingChipItemList.text = tvEntity.vote_average.toString()
            itemView.languageChipItemList.text =
                tvEntity.original_language.toUpperCase(Locale.getDefault())
            itemView.chipGroup2ItemList.removeAllViews()
            for ((chipGenreCount, genre) in tvEntity.genres.split(", ").withIndex())
                if (chipGenreCount <= 1 && genre.isNotEmpty()) addGenreChip(genre)
            itemView.itemCardView.setOnClickListener {
                onFavouriteTvClickListener.onTvItemClick(
                    tvEntity
                )
            }
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