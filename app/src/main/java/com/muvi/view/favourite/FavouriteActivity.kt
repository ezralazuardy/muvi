package com.muvi.view.favourite

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.muvi.R
import com.muvi.config.ContentType
import com.muvi.database.local.entity.MovieEntity
import com.muvi.database.local.entity.TvEntity
import com.muvi.view.detail.DetailActivity
import com.muvi.view.favourite.adapter.movie.OnFavouriteMovieClickListener
import com.muvi.view.favourite.adapter.tv.OnFavouriteTvClickListener
import kotlinx.android.synthetic.main.activity_favourite.*
import org.jetbrains.anko.startActivity

class FavouriteActivity : AppCompatActivity(), OnFavouriteMovieClickListener,
    OnFavouriteTvClickListener {

    private val favouriteActivityViewPagerAdapter by lazy {
        FavouriteActivityViewPagerAdapter(supportFragmentManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)
        setComponent()
    }

    private fun setComponent() {
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewPager.adapter = favouriteActivityViewPagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val drawable: Drawable? = ContextCompat.getDrawable(
                this,
                FavouriteActivityViewPagerAdapter.tabIcons[i]
            )
            drawable?.colorFilter = PorterDuffColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP)
            tabLayout.getTabAt(i)?.icon = drawable
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onMovieItemClick(movieEntity: MovieEntity) {
        startActivity<DetailActivity>(
            "type" to ContentType.MOVIE,
            "movie" to movieEntity,
            "openFrom" to this.javaClass.simpleName
        )
    }

    override fun onTvItemClick(tvEntity: TvEntity) {
        startActivity<DetailActivity>(
            "type" to ContentType.TV,
            "tv" to tvEntity,
            "openFrom" to this.javaClass.simpleName
        )
    }
}
